package com.dwe.springboot.tools.service;

import com.dwe.springboot.tools.model.CarEntity;

import java.util.List;


public interface FileCarStorageService {
    void init();

    void saveRecord(CarEntity car);

    List<String> getAll();

    void deleteCarRecords();
}