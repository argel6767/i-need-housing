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
    String iNeedHousingRepo;

    @ConfigProperty(name = "azure.cron-job-service.repository.name")
    String cronJobServiceRepo;

    @Scheduled(every = "168h")
    void deleteOldINeedHousingImagesJob() {
        Log.info("Running Cron Job, deleting old INeedHousing API images");
        try {
            containerRegistryRestService.deleteOldImages(iNeedHousingRepo);
            Log.info("Successfully deleted old INeedHousing API images");
        }
        catch (JsonProcessingException e) {
            Log.error("Failed to run Cron Job", e);
        }
    }

    @Scheduled(every = "169h")
    void deleteOldCronJobsJob() {
        Log.info("Running Cron Job, deleting old Cron Jobs Service images");
        try {
            containerRegistryRestService.deleteOldImages(cronJobServiceRepo);
            Log.info("Successfully deleted old Cron Job Service images");
        }
        catch (JsonProcessingException e) {
            Log.error("Failed to run Cron Job", e);
        }
    }
}
