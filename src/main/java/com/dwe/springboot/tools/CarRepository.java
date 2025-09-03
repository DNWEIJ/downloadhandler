package com.dwe.springboot.tools;

import com.dwe.springboot.tools.model.CarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<CarEntity, Long> {

    List<CarEntity> findByPersonOrderByDriveDateAsc(String name);
}