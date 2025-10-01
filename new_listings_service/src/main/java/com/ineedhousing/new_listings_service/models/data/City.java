package com.ineedhousing.new_listings_service.models.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    private String cityName;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createdAt;

    public City() {}

    public City(String cityName, Double latitude, Double longitude) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
