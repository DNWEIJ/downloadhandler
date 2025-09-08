package com.dwe.springboot.tools.car.service;


import com.dwe.springboot.tools.car.model.CarEntity;
import com.dwe.springboot.tools.car.model.TripEntity;
import com.dwe.springboot.tools.car.repo.CarRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<String> getAllAsCsv() {
        Iterable<CarEntity> carEntities = carRepository.findAll();
        return StreamSupport.stream(carEntities.spliterator(), false)
                .map(CarEntity::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarEntity> getAllAsList() {
        return new ArrayList<>(carRepository.findAll());
    }

    @Override
    public List<String> getAllNames() {
        return getAllAsList().stream().map(CarEntity::getName).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getAllNameAndTotalKm() {
        return getAllAsList().stream()
                .collect(Collectors.toMap(CarEntity::getName, CarEntity::getKmTotal));
    }

    @Override
    public void saveRecord(TripEntity drive) {
        CarEntity car = carRepository.findByName(drive.getCarType());
        car.setKmTotal(drive.getKmTotal());
        carRepository.save(car);
    }

    @Override
    public Long getLatestTotal(String name) {
        return (long) carRepository.findByName(name).getKmTotal();
    }
}