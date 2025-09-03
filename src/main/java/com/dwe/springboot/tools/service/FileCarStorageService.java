package com.dwe.springboot.tools.service;

import com.dwe.springboot.tools.model.CarEntity;

import java.util.List;


public interface FileCarStorageService {
    void init();

    Long saveRecord(CarEntity car);

    List<String> getAllAsCsv();

    void deleteCarRecords();

    String getHtmlStringOf(Long id);

    List<CarEntity> getAllAsList(String name);
    List<CarEntity> getAllAsList();

    Integer getLatestTotal(String name);
}