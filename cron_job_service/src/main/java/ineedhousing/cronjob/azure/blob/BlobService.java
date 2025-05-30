package ineedhousing.cronjob.azure.blob;

import java.io.File;
import java.io.UncheckedIOException;
import java.time.Instant;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlobService {

    private final BlobContainerClient blobContainerClient;

    public BlobService(BlobContainerClient blobContainerClient) {
        this.blobContainerClient = blobContainerClient;
    }

    public void uploadLogFile(File file) {
        String blobName = Instant.now() + "-" + file.getName();

        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        Log.info("Attempting to upload logs");

        try {
            String filePath = file.getAbsolutePath();
            blobClient.uploadFromFile(filePath);
        }
        catch (UncheckedIOException uioe) {
            Log.errorf("Upload failed!");
            Log.errorf(uioe.toString());
        }
        finally {
            Log.info("Deleting file");
            file.delete();
        }
        Log.info("File uploaded successfully");
    }
}
