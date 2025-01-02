package com.ineedhousing.backend.user_search_preferences;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDate;
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
    private Point cityOfEmployment;

    //will default to a 25 radius circle if not maxRadius is given of whether
    //jobLocation of cityOfEmployment
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Polygon desiredArea;

    @Column(nullable = false)
    private Integer maxRadius = 25;

    private Integer maxRent;

    private String travelType; //future implementation

    @Column(nullable = false)
    private Integer minNumberOfBedRooms = 0;

    @Column(nullable = false)
    private Double minNumberOfBathrooms = 1.0;

    @Column(nullable = false)
    private Boolean isFurnished = false;

    @Column(nullable = false)
    private LocalDate internshipStart;

    @Column(nullable = false)
    private LocalDate internshipEnd;

    private LocalDateTime updatedAt;

}
