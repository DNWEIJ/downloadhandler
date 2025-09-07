package com.dwe.springboot.tools.car.service;

import com.dwe.springboot.tools.car.model.CarEntity;
import com.dwe.springboot.tools.car.model.TripEntity;

import java.util.List;
import java.util.Map;


public interface CarService {

    List<String> getAllAsCsv();

    void deleteCarRecords();

    List<CarEntity> getAllAsList();

    List<String> getAllNames();

    Long getLatestTotal(String name);

    Map<String, Integer> getAllNameAndTotalKm();

    void saveRecord(TripEntity drive);
}