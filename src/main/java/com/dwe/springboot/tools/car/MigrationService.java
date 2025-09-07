package com.dwe.springboot.tools.car;

import com.dwe.springboot.tools.car.model.CarEntity;
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
        long count = carRepository.count();
        if (count == 0) {
            carRepository.save(
                    CarEntity.Builder.builder()
                            .name("VW")
                            .kmTotal(driveRepository.findFirstByCarTypeOrderByKmTotalDesc("VW").getKmTotal())
                            .insurancePerYear(33600)
                            .roadTaxPerYear(96000)
                            .build()
            );

            carRepository.save(
                    CarEntity.Builder.builder()
                            .name("Toyota")
                            .kmTotal(driveRepository.findFirstByCarTypeOrderByKmTotalDesc("Toyota").getKmTotal())
                            .insurancePerYear(33384)
                            .roadTaxPerYear(29600)
                            .build()
            );
        }
    }
}