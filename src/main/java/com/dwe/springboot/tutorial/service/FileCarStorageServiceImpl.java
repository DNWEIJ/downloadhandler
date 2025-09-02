package com.dwe.springboot.tutorial.service;


import com.dwe.springboot.tutorial.CarRepository;
import com.dwe.springboot.tutorial.configuration.CarProperties;
import com.dwe.springboot.tutorial.model.CarEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileCarStorageServiceImpl implements FileCarStorageService {
    private final CarProperties properties;
//    private final CarRepository carRepository;

    FileCarStorageServiceImpl(CarProperties properties, CarRepository carRepository) {
        this.properties = properties;
//        this.carRepository = carRepository;
    }


    public void  init() {
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

    public void saveRecord(CarEntity car)  {
//        carRepository.save(car);
//        try {
//            Files.writeString(
//                Paths.get(properties.location() + "/" + properties.file()),
//                car.toString() + System.lineSeparator(), StandardOpenOption.APPEND
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}