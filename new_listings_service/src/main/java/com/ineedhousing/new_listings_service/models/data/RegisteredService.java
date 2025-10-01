package com.ineedhousing.new_listings_service.models.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class RegisteredService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    private String serviceName;
    private String ApiTokenHash;
    private LocalDateTime createdDate;

    public RegisteredService(String serviceName, String ApiTokenHash) {
        this.serviceName = serviceName;
        this.ApiTokenHash = ApiTokenHash;
    }

    public RegisteredService() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getApiTokenHash() {
        return ApiTokenHash;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
