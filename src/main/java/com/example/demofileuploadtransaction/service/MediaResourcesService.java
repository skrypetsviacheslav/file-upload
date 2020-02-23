package com.example.demofileuploadtransaction.service;

import com.example.demofileuploadtransaction.model.MediaResources;
import com.example.demofileuploadtransaction.repository.MediaResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaResourcesService {
    private final MediaResourcesRepository mediaResourcesRepository;

    @Autowired
    public MediaResourcesService(MediaResourcesRepository mediaResourcesRepository) {
        this.mediaResourcesRepository = mediaResourcesRepository;
    }

    public MediaResources create(MediaResources mediaResources) {
        return mediaResourcesRepository.save(mediaResources);
    }

    public List<MediaResources> findAll() {
        return mediaResourcesRepository.findAll();
    }
}
