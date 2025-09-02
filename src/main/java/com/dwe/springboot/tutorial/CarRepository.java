package com.dwe.springboot.tutorial;

import com.dwe.springboot.tutorial.model.CarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends CrudRepository<CarEntity, Long> {

}