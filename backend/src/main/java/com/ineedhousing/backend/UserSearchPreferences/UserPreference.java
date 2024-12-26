package com.ineedhousing.backend.UserSearchPreferences;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.spatial.JdbcGeographyType; // For PostgreSQL `geography`



@Data
@NoArgsConstructor
@Entity
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @JdbcType(JdbcGeographyType.class)
    private Point
}
