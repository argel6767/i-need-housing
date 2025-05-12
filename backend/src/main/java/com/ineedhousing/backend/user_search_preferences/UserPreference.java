package com.ineedhousing.backend.user_search_preferences;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO add isPetFriendly field
@Data
@NoArgsConstructor
@Entity
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "geometry(Point, 4326)")
    @JsonIgnore
    private Point jobLocation;
    
    @JsonProperty("jobLocation")
     public double[] getJobLocationCoordinates() {
         if (jobLocation != null) {
             return new double[]{jobLocation.getY(), jobLocation.getX()}; //returns as lat, long
         }
         return null;
     }

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false, name = "city_of_employment_location")
    @JsonIgnore
    private Point cityOfEmploymentCoordinates;

    @JsonProperty("cityOfEmploymentCoords")
    public double[] getCityOfEmploymentCoordinates() {
        if (cityOfEmploymentCoordinates != null) {
            return new double[]{jobLocation.getY(), jobLocation.getX()};
        }
        return null;
    }

    private String cityOfEmployment;

    //will default to a 25 radius circle if not maxRadius is given of whether
    //jobLocation of cityOfEmployment
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    @JsonIgnore
    private Polygon desiredArea;

    @JsonProperty("desiredArea")
    public List<Map<String, Double>> getDesiredAreaForGoogleMaps() {
    List<Map<String, Double>> coords = new ArrayList<>();
    for (Coordinate coord : this.desiredArea.getCoordinates()) {
        coords.add(Map.of("lat", coord.y, "lng", coord.x)); //Latitude is Y, Longitude is X
    }
    return coords;
    }

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
