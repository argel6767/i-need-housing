package com.ineedhousing.new_listings_service.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = {
        @Index(name = "idx_address", columnList = "address")
})
public class HousingListing {

    public HousingListing() {}

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

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Point getLocation() {
        return location;
    }

    public static class HousingListingBuilder {
        HousingListing housingListing = new HousingListing();

        public HousingListing build() {
            return housingListing;
        }

        public HousingListingBuilder id(Long id) {
            housingListing.id = id;
            return this;
        }

        public HousingListingBuilder source(String source) {
            housingListing.source = source;
            return this;
        }

        public HousingListingBuilder title(String title) {
            housingListing.title = title;
            return this;
        }

        public HousingListingBuilder description(String description) {
            housingListing.description = description;
            return this;
        }

        public HousingListingBuilder rate(Double rate) {
            housingListing.rate = rate;
            return this;
        }

        public HousingListingBuilder location(Point location) {
            housingListing.location = location;
            return this;
        }

        public HousingListingBuilder address(String address) {
            housingListing.address = address;
            return this;
        }

        public HousingListingBuilder listingUrl(String listingUrl) {
            housingListing.listingUrl = listingUrl;
            return this;
        }

        public HousingListingBuilder imageUrls(List<String> imageUrls) {
            housingListing.imageUrls = imageUrls;
            return this;
        }

        public HousingListingBuilder propertyType(String propertyType) {
            housingListing.propertyType = propertyType;
            return this;
        }

        public HousingListingBuilder numBeds(Integer numBeds) {
            housingListing.numBeds = numBeds;
            return this;
        }

        public HousingListingBuilder numBaths(Double numBaths) {
            housingListing.numBaths = numBaths;
            return this;
        }
    }
}
