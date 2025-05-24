package com.ineedhousing.backend.azure.blob.profile_picture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ineedhousing.backend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class UserProfilePicture {

    @Id
    @Column(unique = true, nullable = false, updatable = false, name = "user_id")
    private Long id;

    private String blobName;
    private String profilePictureUrl;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
