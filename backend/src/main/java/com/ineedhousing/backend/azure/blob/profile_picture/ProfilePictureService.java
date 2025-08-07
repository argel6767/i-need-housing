package com.ineedhousing.backend.azure.blob.profile_picture;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobUploadFromFileOptions;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.ineedhousing.backend.azure.blob.exceptions.UserProfilePictureNotFoundException;
import com.ineedhousing.backend.azure.blob.utils.FileConverter;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.OffsetDateTime;

/**
 * Houses business logic for users to submit profiles pictures to the Azure Blob Container
 */
@Service
@Log
public class ProfilePictureService {


    private final BlobContainerClient blobContainerClient;
    private final ProfilePictureRepository profilePictureRepository;
    private final UserService userService;
    private final RestClient restClient;

    public ProfilePictureService(BlobContainerClient blobContainerClient, ProfilePictureRepository profilePictureRepository, UserService userService, RestClient restClient) {
        this.blobContainerClient = blobContainerClient;
        this.profilePictureRepository = profilePictureRepository;
        this.userService = userService;
        this.restClient = restClient;
    }

    /**
     * generates a SAS URL that allows the frontend to grab the user's profile picture via the signed URL
     * @param id -- grabs the user's Profile Picture Object, should they have one
     * @return SAS URL
     */
    public String generateReadSASURL(Long id) {
        UserProfilePicture profilePicture = getUserProfilePicture(id);
        String blobName = profilePicture.getBlobName();
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        String sasToken = generateSASToken(blobName, blobClient);
        String sasUrl = blobClient.getBlobUrl() + "?" + sasToken;
        log.info("Generated SAS URL: " + sasUrl);
        return sasUrl;
    }

    /**
     * generates a SAS URL that allows the frontend to grab the user's profile picture via the signed URL
     * this should only be used when a user does not have a Profile Picture attached to them yet
     * @param blobName -- the blobName created earlier
     * @return SAS URL
     */
    public String generateReadSASURL(String blobName) { //id used as cache key
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        String sasToken = generateSASToken(blobName, blobClient);
        String sasUrl = blobClient.getBlobUrl() + "?" + sasToken;
        log.info("Generated SAS URL: " + sasUrl);
        return sasUrl;
    }

    /**
     * Generates the actual SAS Token used for the SAS Url
     * only allows for read access via the url
     * @param blobName
     * @param blobClient
     * @return
     */
    private String generateSASToken(String blobName, BlobClient blobClient) {
        log.info("Generating read SAS token for user profile picture " + blobName);
        OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(2); // 2 hours expire
        BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);
        BlobServiceSasSignatureValues serviceSasValues = new BlobServiceSasSignatureValues(expiryTime, blobSasPermission);
        return blobClient.generateSas(serviceSasValues);
    }

    /**
     * generates a new SAS URl and then saves it to allow for a quicker fetch later on if needed, used when the current SAS URL
     * is expired
     * This invalidates the cache as a new url is made
     * @param id
     * @return
     */
    public String updateReadSASURL(Long id) {
        UserProfilePicture profilePicture = getUserProfilePicture(id);
        String sasUrl = generateReadSASURL(id);
        profilePicture.setProfilePictureUrl(sasUrl);
        log.info("Saving new url to User's Profile Picture");
        profilePictureRepository.save(profilePicture); //save new url
        return sasUrl;
    }

    /**
     * Uploads the file into the Blob container and creates its name via the user's ID
     * @param multipartFile
     * @param id
     * @return
     * @throws IOException
     */
    public String[] uploadProfilePicture(MultipartFile multipartFile, Long id) throws IOException {
        String blobName = generateBlobName(id);
        File file = FileConverter.convertMultipartFileToFile(multipartFile);
        File convertedFile = FileConverter.convertFileToJpg(file);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        log.info("Building BlobHttpHeaders");
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType("image/jpg") // or appropriate image type
                .setContentDisposition("inline");

        try {
            log.info("Uploading profile picture to container");
            String path = convertedFile.getAbsolutePath();
            blobClient.uploadFromFile(path,
                    null, // parallelTransferOptions
                    headers, // BlobHttpHeaders
                    null, // metadata
                    null, // tier
                    null, // requestConditions
                    null  );// timeout);
        }
        catch (UncheckedIOException uioe) {
            log.severe("Upload failed!");
            log.severe(uioe.toString());
            throw new IOException("File upload failed!");
        }
        finally { // Clean up temporary file after upload
            log.info("Deleting temporary files");
            convertedFile.delete();
            file.delete();
        }

        //generate a SAS URL for new uploaded blob
        log.info("Profile picture uploaded");
        String sasUrl = generateReadSASURL(blobName);
        log.info("Saving new url to User's Profile Picture");
        return new String[] { sasUrl, blobName };
    }

    @Transactional
    @CacheEvict(key = "#id", value = "url")
    public String updateProfilePicture(MultipartFile multipartFile, Long id) throws IOException {
        log.info("Updating profile picture");
        String[] storageValues = uploadProfilePicture(multipartFile, id);
        UserProfilePicture profilePicture = getUserProfilePicture(id);
        profilePicture.setProfilePictureUrl(storageValues[0]);
        profilePicture.setBlobName(storageValues[1]);
        log.info("Saving new url to User's Profile Picture");
        profilePictureRepository.save(profilePicture);
        return storageValues[0];
    }

    //creates the blob name depending on the users file type
    private String generateBlobName(Long id) {
        return "profiles/" + id + "." + "jpg";
    }

    /**
     * This method should be only used when a user has yet to make a UserProfilePicture Entity attached to them
     * ie they have never submitted a profile. This will create the entity while also allow returning the SAS URL
     * @param multipartFile
     * @param id
     */
    @Transactional
    public String createUserProfilePictureEntity(MultipartFile multipartFile, Long id) throws IOException {
        String[] storageValues = uploadProfilePicture(multipartFile, id);

        log.info("Profile picture entity made");
        UserProfilePicture userProfilePicture = new UserProfilePicture();
        log.info("fetching user entity in DB");
        User user = userService.getUserById(id);

        //map values
        log.info("mapping fields");
        userProfilePicture.setBlobName(storageValues[1]);
        userProfilePicture.setProfilePictureUrl(storageValues[0]);
        userProfilePicture.setUser(user);
        profilePictureRepository.save(userProfilePicture);
        return storageValues[0];
    }

    /**
     * Grabs the user SAS URL, this is only used when the URL is not expired
     * the url is also cached using the id as a key for faster retrieval
     * @param id
     * @return
     */
    @Cacheable(key = "#id", value = "url")
    public String getSASUrl(Long id) {
        UserProfilePicture userProfilePicture = getUserProfilePicture(id);
        String url = userProfilePicture.getProfilePictureUrl();
        if (isSignedUrlExpired(url)) {
            return updateReadSASURL(id);
        }
        return url;
    }

    private boolean isSignedUrlExpired(String url) {
        ResponseEntity<Void> response = restClient.
                head()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
        return !response.getStatusCode().is2xxSuccessful();
    }

    /**
     * grabs a user's profile picture object's
     * will throw an object if none can be found
     * @param id
     * @return UserProfilePicture
     */
    public UserProfilePicture getUserProfilePicture(Long id) {
        log.info("Retrieving user profile picture");
        return profilePictureRepository.findById(id)
                .orElseThrow(() -> new UserProfilePictureNotFoundException("Profile Picture not found with id: " + id));
    }

    /**
     * Deletes UserProfilePicture entry while also making sure to delete the user's actual profile picture uploaded to the blob container
     * @param id
     */
    @CacheEvict(key = "#id", value = "url")
    public void deleteUserProfilePicture(Long id) {
        log.info("Deleting user's profile picture in container blob");
        UserProfilePicture userProfilePicture = getUserProfilePicture(id);
        blobContainerClient.getBlobClient(userProfilePicture.getBlobName()).delete();
        log.info("Deleting user profile picture");
        profilePictureRepository.delete(userProfilePicture);
    }


}
