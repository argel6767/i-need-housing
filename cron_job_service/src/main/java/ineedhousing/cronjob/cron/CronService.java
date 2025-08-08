package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.azure.container_registry.ContainerRegistryRestService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

@ApplicationScoped
public class CronService {

    @Inject
    ContainerRegistryRestService containerRegistryRestService;

    @ConfigProperty(name = "azure.i-need-housing.repository.name")
    String repositoryName;

    @Scheduled(every = "168hr")
    void deleteOldImagesJob() {
        Log.info("Running Cron Job, deleting old INeedHousing API images");
        try {
            containerRegistryRestService.deleteOldImages(repositoryName);
            Log.info("Successfully deleted old INeedHousing API images");
        }
        catch (JsonProcessingException e) {
            Log.error("Failed to run Cron Job", e);
        }
    }
}
