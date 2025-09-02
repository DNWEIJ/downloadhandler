package com.dwe.springboot.tutorial.service;

import com.dwe.springboot.tutorial.model.CarEntity;


public interface FileCarStorageService {
    void init();

    void saveRecord(CarEntity car);

}