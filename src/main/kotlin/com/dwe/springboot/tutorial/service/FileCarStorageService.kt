package com.dwe.springboot.tutorial.service

import com.dwe.springboot.tutorial.model.CarRecord

interface FileCarStorageService {
    fun init()
    fun saveRecord(car: CarRecord)

}