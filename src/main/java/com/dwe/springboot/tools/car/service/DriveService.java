package com.dwe.springboot.tools.car.service;

import com.dwe.springboot.tools.car.model.TripEntity;

import java.util.List;


public interface DriveService {

    Long saveRecord(TripEntity car);

    List<String> getAllAsCsv();

    void deleteCarRecords();

    String getHtmlStringOf(Long id);

    List<TripEntity> getAllAsList(String name);
    List<TripEntity> getAllAsList();

    Integer getLatestTotal(String name);
}