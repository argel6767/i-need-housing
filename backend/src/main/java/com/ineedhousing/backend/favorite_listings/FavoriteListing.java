package com.ineedhousing.backend.favorite_listings;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FavoriteListing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @NonNull
    @OneToOne
    @JoinColumn(name = "listing_id", referencedColumnName = "id")
    @ToString.Exclude
    private HousingListing housingListing;

    @Timestamp
    private LocalDateTime createdAt;

    /**
     * on creation captures the time
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
