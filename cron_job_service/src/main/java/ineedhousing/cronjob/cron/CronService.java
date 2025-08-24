package ineedhousing.cronjob.cron;

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

@ApplicationScoped
public class CronService {

    @Inject
    ContainerRegistryService containerRegistryService;

    @Inject
    DatabaseService databaseService;

    @Inject
    ArtifactRegistryService artifactRegistryService;

    @Inject
    Config config;

    @Inject
    LogService logService;

    private String iNeedHousingRepo;
    private String cronJobServiceRepo;

    private String gcpProject;
    private String gcpRegistryLocation;
    private String gcpRepository;

    @PostConstruct
    void init() {
        iNeedHousingRepo = config.getOptionalValue("azure.i-need-housing.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("INeedHousing Repo name not found!"));

        cronJobServiceRepo = config.getOptionalValue("azure.cron-job-service.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Cron_Job_Service Repo name not found!"));

        gcpProject = config.getOptionalValue("gcp.project.id", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("GCP_Project id not found!"));

        gcpRegistryLocation = config.getOptionalValue("gcp.registry.location", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("GCP registry location not found!"));

        gcpRepository = config.getOptionalValue("gcp.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("GCP registry repository not found!"));
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
        Log.info("Running Cron Job, deleting old Cron Jobs Service images");
        try {
            containerRegistryService.deleteOldImages(cronJobServiceRepo);
            logService.publish("Successfully deleted old Cron Job Service images", LoggingLevel.INFO);
        }
        catch (JsonProcessingException e) {
            logService.publish("Failed to delete old Cron Job Service images\n" + e, LoggingLevel.ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 4,11,18,25 * ?") // Midnight on the 4th, 11th, 18th, and 25th
    void deleteOldNewListingsServiceImagesJob() {
        Log.info("Running Cron Job, deleting old New Listings Service images");
        try {
            artifactRegistryService.deleteOldImages(gcpProject, gcpRegistryLocation, gcpRepository);
            logService.publish("Successfully deleted old New Listings Service images", LoggingLevel.INFO);
        }
        catch (Exception e) {
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
