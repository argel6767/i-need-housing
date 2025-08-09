package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.azure.container_registry.ContainerRegistryRestService;
import ineedhousing.cronjob.db.DatabaseService;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

@ApplicationScoped
public class CronService {

    @Inject
    ContainerRegistryRestService containerRegistryRestService;

    @Inject
    DatabaseService databaseService;

    @Inject
    Config config;

    private String iNeedHousingRepo;
    private String cronJobServiceRepo;

    @PostConstruct
    void init() {
        iNeedHousingRepo = config.getOptionalValue("azure.i-need-housing.repository.name", String.class)
                .orElseThrow(() -> new RuntimeException("INeedHousing Repo name not found!"));

        cronJobServiceRepo = config.getOptionalValue("azure.cron-job-service.repository.name", String.class)
                .orElseThrow(() -> new RuntimeException("Cron_Job_Service Repo name not found!"));
    }

    @Scheduled(every = "168h") // every 7 days
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

    @Scheduled(every = "169h") // every 7 days + 1 hour
    void deleteOldCronJobServiceImagesJob() {
        Log.info("Running Cron Job, deleting old Cron Jobs Service images");
        try {
            containerRegistryRestService.deleteOldImages(cronJobServiceRepo);
            Log.info("Successfully deleted old Cron Job Service images");
        }
        catch (JsonProcessingException e) {
            Log.error("Failed to run Cron Job", e);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?") //First of every month
    void deleteOldHousingListingsJob() {
        Log.info("Running Cron Job, deleting old Housing Listings");
        try {
            databaseService.deleteOldListings();
        }
        catch (Exception e) {
            Log.error("Failed to run Cron Job", e);
        }
    }
}
