package ineedhousing.cronjob.azure.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.UncheckedIOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BlobServiceTest {

    @Test
    void testUploadLogFile_WithSuccessfulUpload() throws Exception {
        // Arrange
        BlobContainerClient blobContainerClient = mock(BlobContainerClient.class);
        BlobClient blobClient = mock(BlobClient.class);
        when(blobContainerClient.getBlobClient(anyString())).thenReturn(blobClient);

        File tempFile = File.createTempFile("test-log", ".txt");
        tempFile.deleteOnExit();

        BlobService blobService = new BlobService(blobContainerClient);

        // Act
        blobService.uploadLogFile(tempFile);

        // Assert
        verify(blobContainerClient, times(1)).getBlobClient(anyString());
        verify(blobClient, times(1)).uploadFromFile(tempFile.getAbsolutePath());
        assert !tempFile.exists();
    }

    @Test
    void testUploadLogFile_WithFailedUpload() throws Exception {
        // Arrange
        BlobContainerClient blobContainerClient = mock(BlobContainerClient.class);
        BlobClient blobClient = mock(BlobClient.class);
        when(blobContainerClient.getBlobClient(anyString())).thenReturn(blobClient);
        
        doThrow(new UncheckedIOException(new java.io.IOException("Test exception")))
            .when(blobClient).uploadFromFile(anyString());

        File tempFile = File.createTempFile("test-log", ".txt");
        tempFile.deleteOnExit();

        BlobService blobService = new BlobService(blobContainerClient);

        // Act
        blobService.uploadLogFile(tempFile);

        // Assert
        verify(blobContainerClient, times(1)).getBlobClient(anyString());
        verify(blobClient, times(1)).uploadFromFile(tempFile.getAbsolutePath());
        assert !tempFile.exists(); // File should still be deleted even on failure
    }
}
