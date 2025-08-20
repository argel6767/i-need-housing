package com.ineedhousing.backend.housing_listings;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
//TODO add date field, such as listing or availability date
@Data
@NoArgsConstructor
@Entity
public class HousingListing {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String title;

    private String description;

    private Double rate;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)", unique = true)
    @JsonIgnore
    private Point location;
     // Custom getter to expose coordinates in JSON
     @JsonProperty("coordinates")
     public double[] getCoordinates() {
         if (location != null) {
             return new double[]{location.getY(), location.getX()}; //returns as lat, long
         }
         return null;
     }
     @JsonProperty("coordinates")
    public void setCoordinates(double[] coordinates) {
    if (coordinates != null && coordinates.length == 2) {
        // Create a Point using the GeometryFactory
        GeometryFactory geometryFactory = new GeometryFactory();
        this.location = geometryFactory.createPoint(new Coordinate(coordinates[1], coordinates[0]));
    }
    }

    @Column(nullable = false)
    private String address;

    @Column()
    private String listingUrl;

    @ElementCollection
    @CollectionTable(name = "housing_listings_pictures", joinColumns = @JoinColumn(name = "housing_listing_id"))
    private List<String> imageUrls;

    private String propertyType;

    private Integer numBeds;

    private Double numBaths;

    private Boolean isPetFriendly;

    private Boolean isFurnished;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
