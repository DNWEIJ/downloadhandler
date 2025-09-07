package com.dwe.springboot.tools.car.repo;

import com.dwe.springboot.tools.car.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    CarEntity findByName(String name);
}