package com.ineedhousing.backend.azure.blob.profile_picture;

import com.ineedhousing.backend.azure.blob.exceptions.UserProfilePictureNotFoundException;
import com.ineedhousing.backend.jwt.JwtUtils;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@RestController
@RequestMapping("/profile-pictures")
@Log
public class ProfilePictureController {

    private final ProfilePictureService profilePictureService;

    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    /**
     * generates new SAS URL for user, typically used when the current is expired
     * then updates it in DB
     * @param
     * @return
     */
    @PutMapping("/url")
    public ResponseEntity<String> updateURL() {
        try {
            Long id = JwtUtils.getCurrentUserId();
            String sasURL = profilePictureService.updateReadSASURL(id);
            return new ResponseEntity<>(sasURL, HttpStatus.CREATED);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * returns SAS URL of user
     * @return
     */
    @GetMapping("/url")
    public ResponseEntity<String> getURL() {
        try {
            Long id = JwtUtils.getCurrentUserId();
            String sasURL = profilePictureService.getSASUrl(id);
            return new ResponseEntity<>(sasURL, HttpStatus.OK);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            log.severe(uppnfe.toString());
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * uploads file then returns its url
     * @param file
     * @return
     */
    @PutMapping("/upload")
    public ResponseEntity<String> updatePicture(@RequestParam("file") MultipartFile file) {
        try {
            Long id = JwtUtils.getCurrentUserId();
            log.info("Uploading file " + file.getOriginalFilename());
            log.info("for user: "+ id);
            String url = profilePictureService.updateProfilePicture(file, id);
            return new ResponseEntity<>(url, HttpStatus.CREATED);
        }
        catch (IOException ioe) {
            log.severe(ioe.toString());
            return new ResponseEntity<>(ioe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            log.severe(uppnfe.toString());
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException | IllegalStateException e) {
            log.severe(e.toString());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (MaxUploadSizeExceededException musee) {
            log.severe(musee.toString());
            return new ResponseEntity<>(musee.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        }
    }

    /**
     * Returns user's Profile Picture
     * @return
     */
    @GetMapping()
    public ResponseEntity<?> getUserProfilePicture() {
        try {
            Long id = JwtUtils.getCurrentUserId();
            log.info("fetching user profile picture for id " + id);
            UserProfilePicture userProfilePicture = profilePictureService.getUserProfilePicture(id);
            return new ResponseEntity<>(userProfilePicture, HttpStatus.OK);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            log.severe(uppnfe.toString());
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * creates new user Profile picture
     * @param file
     * @return
     */
    @PostMapping()
    public ResponseEntity<String> createUserProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            Long id = JwtUtils.getCurrentUserId();
            log.info("Creating user profile picture for user " + id);
            String sasURL = profilePictureService.createUserProfilePictureEntity(file, id);
            return new ResponseEntity<>(sasURL, HttpStatus.CREATED);
        }
        catch (IOException ioe) {
            log.severe(ioe.toString());
            return new ResponseEntity<>(ioe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            log.severe(uppnfe.toString());
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException | IllegalStateException e) {
            log.severe(e.toString());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (MaxUploadSizeExceededException musee) {
            log.severe(musee.toString());
            return new ResponseEntity<>(musee.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        }
    }

    /**
     * deletes user's Profile Picture
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<String> deleteUserProfilePicture() {
        try {
            Long id = JwtUtils.getCurrentUserId();
            log.info("Deleting user profile picture for id " + id);
            profilePictureService.deleteUserProfilePicture(id);
            return new ResponseEntity<>("profile picture deleted!",HttpStatus.NO_CONTENT);
        }
        catch (UserProfilePictureNotFoundException uppnfe) {
            log.severe(uppnfe.toString());
            return new ResponseEntity<>(uppnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
