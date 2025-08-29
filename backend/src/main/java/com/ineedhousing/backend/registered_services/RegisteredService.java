package com.ineedhousing.backend.registered_services;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
