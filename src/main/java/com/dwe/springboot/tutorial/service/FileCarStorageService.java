package com.dwe.springboot.tutorial.service;

import com.dwe.springboot.tutorial.model.CarEntity;

import java.util.List;


public interface FileCarStorageService {
    void init();

    void saveRecord(CarEntity car);

    List<String> getAll();

    void deleteCarRecords();
}