package com.ineedhousing.backend.user_search_preferences;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.ToString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "geometry(Point, 4326)")
    @ToString.Exclude
    private Point jobLocation;


    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false, name = "city_of_employment_location")
    @ToString.Exclude
    private Point cityOfEmploymentCoordinates;


    private String cityOfEmployment;

    //will default to a 25 radius circle if not maxRadius is given of whether
    //jobLocation of cityOfEmployment
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    @ToString.Exclude
    private Polygon desiredArea;


    @Column(nullable = false)
    private Integer maxRadius = 25;

    private Double maxRent=10000.0;

    private String travelType; //future implementation

    @Column(name = "min_number_of_bedrooms", nullable = false)
    private Integer minNumberOfBedrooms = 0;

    @Column(nullable = false)
    private Double minNumberOfBathrooms = 1.0;

    @Column(nullable = false)
    private Boolean isFurnished = false;

    @Column(nullable = true)
    private LocalDate internshipStart;

    @Column(nullable = true)
    private LocalDate internshipEnd;

    private LocalDateTime updatedAt;

}
