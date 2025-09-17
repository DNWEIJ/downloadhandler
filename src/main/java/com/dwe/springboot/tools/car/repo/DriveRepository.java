package com.dwe.springboot.tools.car.repo;

import com.dwe.springboot.tools.car.model.TripEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriveRepository extends JpaRepository<TripEntity, Long> {

    List<TripEntity> findByPersonOrderByDriveDateAsc(String name);

    TripEntity findFirstByCarTypeOrderByKmTotalDesc(String carType);

    default List<TripEntity> findAllOrderByDriveDateAsc() {
        Sort sortBy = Sort.by(Sort.Direction.ASC, "driveDate")
                .and(Sort.by(Sort.Direction.ASC, "id"));
        return findAll(sortBy);
    }
}