package com.ineedhousing.backend.keymaster_service;

import com.ineedhousing.backend.keymaster_service.models.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {

    Optional<RegisteredService> findByServiceName(String name);
}
