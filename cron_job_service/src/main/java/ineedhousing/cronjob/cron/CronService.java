package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.keymaster.KeymasterWebhookService;
import ineedhousing.cronjob.keymaster.models.RotatingKeyEvent;
import org.eclipse.microprofile.config.Config;

import com.fasterxml.jackson.core.JsonProcessingException;

import ineedhousing.cronjob.azure.container_registry.ContainerRegistryService;
import ineedhousing.cronjob.azure.postgres.DatabaseService;
import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import ineedhousing.cronjob.gcp.artifact_registry.ArtifactRegistryService;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class CronService {

    @Inject
    ContainerRegistryService containerRegistryService;

    @Inject
    DatabaseService databaseService;

    @Inject
    Config config;

    @Inject
    LogService logService;

    @Inject
    KeymasterWebhookService keymasterWebhookService;

    private String iNeedHousingRepo;
    private String cronJobServiceRepo;
    private String newListingsServiceRepo;

    @PostConstruct
    void init() {
        iNeedHousingRepo = config.getOptionalValue("azure.i-need-housing.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("INeedHousing Repo name not found!"));

        cronJobServiceRepo = config.getOptionalValue("azure.cron-job-service.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Cron_Job_Service Repo name not found!"));

        newListingsServiceRepo = config.getOptionalValue("azure.new-listings-service.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("New Listing Service Repo name not found!"));
    }

    @Scheduled(cron = "0 0 0 2,9,16,23 * ?") // Midnight on the 2nd, 9th, 16th, and 23rd
    void deleteOldINeedHousingImagesJob() {
        logService.publish("Running Cron Job, deleting old INeedHousing API images", LoggingLevel.INFO);
        try {
            containerRegistryService.deleteOldImages(iNeedHousingRepo);
            logService.publish("Successfully deleted old INeedHousing API images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old INeedHousing API images\n" + e, LoggingLevel.ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 3,10,17,24 * ?") // Midnight on the 3rd, 10th, 17th, and 24th
    void deleteOldCronJobServiceImagesJob() {
        logService.publish("Running Cron Job, deleting old Cron Jobs Service images", LoggingLevel.INFO);
        try {
            containerRegistryService.deleteOldImages(cronJobServiceRepo);
            logService.publish("Successfully deleted old Cron Job Service images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old Cron Job Service images\n" + e, LoggingLevel.ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 4,11,18,25 * ?")
    void deleteNewListingsServiceImagesJob() {
        logService.publish("Running Cron Job, deleting old New Listings Service images", LoggingLevel.INFO);
        try {
            containerRegistryService.deleteOldImages(newListingsServiceRepo);
            logService.publish("Successfully deleted old New Listings Service images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old New Listings Service images\n" + e, LoggingLevel.ERROR);
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

    @Scheduled(cron = "0 0 0 1,8,15,22 * ?") // Midnight on the 1st, 8th, 15th, and 22nd
    void triggerRegistrationRotationJob() {
        logService.publish("Running Cron Job, triggering registration rotation", LoggingLevel.INFO);
        keymasterWebhookService.triggerRegistrationKeyRotation(new RotatingKeyEvent("Requesting new service registration key for Keymaster Service", LocalDateTime.now()));
    }
}
