package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.azure.container_registry.ContainerRegistryRestService;
import ineedhousing.cronjob.azure.postgres.DatabaseService;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.Config;

import com.fasterxml.jackson.core.JsonProcessingException;

@ApplicationScoped
public class CronService {

    @Inject
    ContainerRegistryRestService containerRegistryRestService;

    @Inject
    DatabaseService databaseService;

    @Inject
    Config config;

    @Inject
    LogService logService;

    private String iNeedHousingRepo;
    private String cronJobServiceRepo;

    @PostConstruct
    void init() {
        iNeedHousingRepo = config.getOptionalValue("azure.i-need-housing.repository.name", String.class)
                .orElseThrow(() -> new RuntimeException("INeedHousing Repo name not found!"));

        cronJobServiceRepo = config.getOptionalValue("azure.cron-job-service.repository.name", String.class)
                .orElseThrow(() -> new RuntimeException("Cron_Job_Service Repo name not found!"));
    }

    @Scheduled(cron = "0 0 0 2,9,16,23 * ?") // Midnight on the 2nd, 9th, 16th, and 23rd
    void deleteOldINeedHousingImagesJob() {
        logService.publish("Running Cron Job, deleting old INeedHousing API images", LoggingLevel.INFO);
        try {
            containerRegistryRestService.deleteOldImages(iNeedHousingRepo);
            logService.publish("Successfully deleted old INeedHousing API images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old INeedHousing API images\n" + e, LoggingLevel.ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 3,10,17,24 * ?") // Midnight on the 3rd, 10th, 17th, and 24th
    void deleteOldCronJobServiceImagesJob() {
        Log.info("Running Cron Job, deleting old Cron Jobs Service images");
        try {
            containerRegistryRestService.deleteOldImages(cronJobServiceRepo);
            logService.publish("Successfully deleted old Cron Job Service images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old Cron Job Service images\n" + e, LoggingLevel.ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?") //First of every month
    void deleteOldHousingListingsJob() {
        Log.info("Running Cron Job, deleting old Housing Listings");
        try {
            databaseService.deleteOldListings();
            logService.publish("Successfully deleted old House Listings", LoggingLevel.INFO);
        }
        catch (Exception e) {
            logService.publish("Failed to delete old House Listings\n" + e, LoggingLevel.ERROR);
        }
    }
}
