package com.dwe.springboot.tools.service;


import com.dwe.springboot.tools.CarRepository;
import com.dwe.springboot.tools.configuration.CarProperties;
import com.dwe.springboot.tools.model.CarEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileCarStorageServiceImpl implements FileCarStorageService {
    private final CarProperties properties;
    private final CarRepository carRepository;

    FileCarStorageServiceImpl(CarProperties properties, CarRepository carRepository) {
        this.properties = properties;
        this.carRepository = carRepository;
    }


    public void init() {
        try {
            Path rootLocation = Paths.get(properties.location());
            Files.createDirectories(rootLocation);
            Files.createFile(Paths.get(properties.location() + "/" + properties.file()));
        } catch (IOException exception) {
            if (exception instanceof java.nio.file.FileAlreadyExistsException) {
                return;
            }
            throw new RuntimeException("Could not initialize storage");

        }
    }

    public Long saveRecord(CarEntity car) {
        try {
            Files.writeString(
                    Paths.get(properties.location() + "/" + properties.file()),
                    car.toString() + System.lineSeparator(), StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            // no action
        }
        return carRepository.save(car).getId();
    }

    @Override
    public List<String> getAllAsCsv() {
        Iterable<CarEntity> carEntities = carRepository.findAll();
        carEntities.forEach(car -> System.out.println(car.toString()));

        return StreamSupport.stream(carEntities.spliterator(), false)
                .map(CarEntity::toString)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCarRecords() {
        carRepository.deleteAll();
    }

    @Override
    public String getHtmlStringOf(Long id) {
        Optional<CarEntity> car = carRepository.findById(id);
        return (car.isPresent()) ? car.get().toHtmlString() : "";
    }

    @Override
    public List<CarEntity> getAllAsList(String name) {
        return carRepository.findByPersonOrderByDriveDateAsc(name);
    }
}