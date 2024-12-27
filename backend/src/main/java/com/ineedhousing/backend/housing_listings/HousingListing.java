package com.ineedhousing.backend.housing_listings;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

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

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Point location;

    @Column(nullable = false)
    private String listingUrl;

    @ElementCollection
    @CollectionTable(name = "housing_listings_pictures", joinColumns = @JoinColumn(name = "housing_listing_id"))
    private List<String> imageUrl;

    private Boolean isPetFriendly;

    private Boolean isFurnished;

}
