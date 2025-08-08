package ineedhousing.cronjob.azure.blob;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * Configuration beans to access the logging blob storage
 */
@ApplicationScoped
public class BlobConfiguration {

    @ConfigProperty(name = "azure.storage.account.name")
    String accountName;

    @ConfigProperty(name = "azure.storage.account.key")
    String accountKey;

    @ConfigProperty(name = "azure.storage.container.name")
    String containerName;

    @Produces
    @ApplicationScoped
    BlobServiceClient blobServiceClient() {
        // Create SharedKeyCredential with account name and key
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(
                accountName,
                accountKey
        );

        String endpoint = String.format("https://%s.blob.core.windows.net", accountName);

        return new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();
    }

    @Produces
    @ApplicationScoped
    BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient) {
        return blobServiceClient.getBlobContainerClient(containerName);
    }
    
}
