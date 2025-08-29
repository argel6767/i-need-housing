package com.ineedhousing.backend.registered_services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {

    Optional<RegisteredService> findByServiceName(String name);
}
