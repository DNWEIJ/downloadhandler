package com.dwe.springboot.tutorial.service;


import com.dwe.springboot.tutorial.configuration.CarProperties;
import com.dwe.springboot.tutorial.model.CarRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileCarStorageServiceImpl {
    private final CarProperties properties;

    FileCarStorageServiceImpl(CarProperties properties) {
        this.properties = properties;
    }


    void  init() {
        try {
            Path rootLocation = Paths.get(properties.location());
            Files.createDirectories(rootLocation);
            Files.createFile(Paths.get(properties.location() + "/" + properties.file()));
        } catch (IOException exception) {
            if (exception instanceof  java.nio.file.FileAlreadyExistsException) {
                return;
            } else {
                throw new RuntimeException("Could not initialize storage");
            }

        }
    }

    void saveRecord(CarRecord car) throws IOException {

        Files.writeString(
            Paths.get(properties.location() + "/" + properties.file()),
            car.toString() + System.lineSeparator(), StandardOpenOption.APPEND
        );
    }
}