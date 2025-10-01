package com.ineedhousing.new_listings_service.repositories;

import com.ineedhousing.new_listings_service.models.data.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCityName(String cityName);

    void deleteByCityName(String cityName);
}
