package com.dwe.springboot.tools.car;

import com.dwe.springboot.tools.car.repo.CarRepository;
import com.dwe.springboot.tools.car.repo.DriveRepository;
import org.springframework.stereotype.Service;

@Service
public class MigrationService {

    private final CarRepository carRepository;
    private final DriveRepository driveRepository;

    public MigrationService(CarRepository carRepository, DriveRepository driveRepository) {
        this.carRepository = carRepository;
        this.driveRepository = driveRepository;
    }

    public void init() {
        // no migration to execute
    }
}