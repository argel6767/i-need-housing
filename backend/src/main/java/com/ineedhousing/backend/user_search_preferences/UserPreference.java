package com.ineedhousing.backend.user_search_preferences;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point jobLocation;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false, name = "city_of_employment_location")
    private Point cityOfEmploymentLocation;

    @Column(nullable = false)
    private Integer maxRadius = 25;

    @Column(nullable = false)
    private Integer minRadius = 0;

    @Column(nullable = false)
    private Integer maxRent = 5000;

    @Column(nullable = false)
    private Integer minRent = 0;

    private String travelType; //future implementation

    @Column(nullable = false)
    private Integer numOfBedrooms;

    @Column(nullable = false)
    private Integer numOfBathrooms;

    @Column(nullable = false)
    private Boolean isFurnished;

    private LocalDateTime updatedAt;

}
