package com.dwe.springboot.tools.download.service;

import com.dwe.springboot.tools.download.configuration.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final StorageProperties properties;

    FileStorageServiceImpl(StorageProperties properties) {
        this.properties = properties;
    }


    public void init() {
        try {
            Path rootLocation = Paths.get(properties.location());
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage");
        }
    }

    public void saveFile(MultipartFile file) {
        try {
            Path rootLocation = Paths.get(properties.location());
            String originalFilename = Boolean.parseBoolean(file.getOriginalFilename()) ? file.getOriginalFilename() : "";
            if (file.isEmpty() || originalFilename.isBlank())
                throw new RuntimeException("Failed to store empty file.");

            Path destinationFile = rootLocation.resolve(
                    Paths.get(originalFilename)
            ).normalize().toAbsolutePath();

            if (destinationFile.getParent() != rootLocation.toAbsolutePath()) {
                throw new RuntimeException("Cannot store file outside current directory");
            }

            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            StringUtils.cleanPath(originalFilename);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}