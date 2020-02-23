package com.example.demofileuploadtransaction.service.transaction;

import com.example.demofileuploadtransaction.exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleanupTransactionListener extends TransactionSynchronizationAdapter {
    private Resource outputFile;

    private Path root = Paths.get("E:\\Temp\\temp-dir");

    public CleanupTransactionListener(Resource outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void afterCompletion(int status) {
        if (STATUS_COMMITTED != status) {
            if (outputFile.exists()) {
                String filename = outputFile.getFilename();
                try {
                    Path path = root.resolve(filename);
                    if (!FileSystemUtils.deleteRecursively(path)) {
                        System.out.println("Could not delete File"
                                + path + " after failed transaction");
                        throw new StorageException("Failed to store file " + path);
                    }
                } catch (IOException e) {
                    throw new StorageException("Failed to store file " + filename, e);
                }
            }
        }
    }
}
