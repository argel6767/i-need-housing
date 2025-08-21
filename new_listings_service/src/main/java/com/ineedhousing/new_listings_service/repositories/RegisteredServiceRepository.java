package com.ineedhousing.new_listings_service.repositories;

import com.ineedhousing.new_listings_service.models.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {

    Optional<RegisteredService> findByServiceName(String name);
}
