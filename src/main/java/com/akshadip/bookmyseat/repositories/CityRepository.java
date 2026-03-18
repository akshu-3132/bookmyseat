package com.akshadip.bookmyseat.repositories;

import com.akshadip.bookmyseat.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    City save(City name);

    Optional<City> findById(Long id);
}
