package com.dwe.springboot.tutorial.service;

import com.dwe.springboot.tutorial.model.CarRecord;

public interface FileCarStorageService {
    void init();
    void saveRecord(CarRecord car);

}