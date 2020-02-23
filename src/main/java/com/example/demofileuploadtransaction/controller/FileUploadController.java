package com.example.demofileuploadtransaction.controller;

import com.example.demofileuploadtransaction.exception.StorageFileNotFoundException;
import com.example.demofileuploadtransaction.model.MediaResources;
import com.example.demofileuploadtransaction.service.MediaResourcesService;
import com.example.demofileuploadtransaction.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final MediaResourcesService mediaResourcesService;

    @Autowired
    public FileUploadController(StorageService storageService, MediaResourcesService mediaResourcesService) {
        this.storageService = storageService;
        this.mediaResourcesService = mediaResourcesService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {

//        model.addAttribute("files",
//                storageService.loadAll().map(
//                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                                "serveFile", path.getFileName().toString()).build().toUri().toString())
//                        .map(x -> new MediaResources(1L, MediaType.IMAGE_PNG_VALUE, x))
//                        .collect(Collectors.toList()));

        model.addAttribute("files",
                mediaResourcesService.findAll().stream()
                        .peek(media -> media.setLocation(MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", media.getLocation()).build().toUri().toString()))
                        .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,
                Files.probeContentType(file.getFile().toPath())).body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
