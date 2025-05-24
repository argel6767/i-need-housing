package com.ineedhousing.backend.azure.blob.profile_picture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<UserProfilePicture, Long> {
}
