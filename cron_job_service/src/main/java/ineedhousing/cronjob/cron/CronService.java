package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.cron.models.JobEvent;
import ineedhousing.cronjob.cron.models.JobStatus;
import ineedhousing.cronjob.keymaster.KeymasterWebhookService;
import ineedhousing.cronjob.keymaster.models.RotatingKeyEvent;

import jakarta.enterprise.event.Event;
import org.eclipse.microprofile.config.Config;

import com.fasterxml.jackson.core.JsonProcessingException;

import ineedhousing.cronjob.azure.container_registry.ContainerRegistryService;
import ineedhousing.cronjob.azure.postgres.DatabaseService;
import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
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

    @Inject
    Event<JobEvent> publisher;

    private String iNeedHousingRepo;
    private String cronJobServiceRepo;
    private String newListingsServiceRepo;

    private static final String DELETE_INEEDHOUSING_IMAGES_JOB = "deleteOldINeedHousingImagesJob";
    private static final String DELETE_CRONJOB_IMAGES_JOB = "deleteOldCronJobServiceImagesJob";
    private static final String DELETE_NEW_LISTINGS_IMAGES_JOB = "deleteNewListingsServiceImagesJob";
    private static final String DELETE_HOUSING_LISTINGS_JOB = "deleteOldHousingListingsJob";
    private static final String TRIGGER_ROTATION_JOB = "triggerRegistrationRotationJob";

    @PostConstruct
    void init() {
        iNeedHousingRepo = config
                .getOptionalValue("azure.i-need-housing.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("INeedHousing Repo name not found!"));

        cronJobServiceRepo = config
                .getOptionalValue("azure.cron-job-service.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Cron_Job_Service Repo name not found!"));

        newListingsServiceRepo = config
                .getOptionalValue("azure.new-listings-service.repository.name", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("New Listing Service Repo name not found!"));
    }

    // ========== WRAPPER METHODS (used only by Quarkus scheduler) ==========
    @Scheduled(cron = "0 0 0 2,9,16,23 * ?")
    void scheduledDeleteOldINeedHousingImagesJob() {
        runDeleteOldINeedHousingImagesJob();
    }

    @Scheduled(cron = "0 0 0 3,10,17,24 * ?")
    void scheduledDeleteOldCronJobServiceImagesJob() {
        runDeleteOldCronJobServiceImagesJob();
    }

    @Scheduled(cron = "0 0 0 4,11,18,25 * ?")
    void scheduledDeleteNewListingsServiceImagesJob() {
        runDeleteNewListingsServiceImagesJob();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    void scheduledDeleteOldHousingListingsJob() {
        runDeleteOldHousingListingsJob();
    }

    @Scheduled(cron = "0 0 0 1,8,15,22 * ?")
    void scheduledTriggerRegistrationRotationJob() {
        runTriggerRegistrationRotationJob();
    }

    // ========== PUBLIC JOB LOGIC METHODS ==========
    public void runDeleteOldINeedHousingImagesJob() {
        logService.publish("Running Cron Job, deleting old INeedHousing API images", LoggingLevel.INFO);
        executeImageDeletion(DELETE_INEEDHOUSING_IMAGES_JOB, () ->
                containerRegistryService.deleteOldImages(iNeedHousingRepo));
    }

    public void runDeleteOldCronJobServiceImagesJob() {
        logService.publish("Running Cron Job, deleting old Cron Job Service images", LoggingLevel.INFO);
        executeImageDeletion(DELETE_CRONJOB_IMAGES_JOB, () ->
                containerRegistryService.deleteOldImages(cronJobServiceRepo));
    }

    public void runDeleteNewListingsServiceImagesJob() {
        logService.publish("Running Cron Job, deleting old New Listings Service images", LoggingLevel.INFO);
        executeImageDeletion(DELETE_NEW_LISTINGS_IMAGES_JOB, () ->
                containerRegistryService.deleteOldImages(newListingsServiceRepo));
    }

    public void runDeleteOldHousingListingsJob() {
        Log.info("Running Cron Job, deleting old Housing Listings");
        JobStatus jobStatus = JobStatus.FAILED;
        try {
            databaseService.deleteOldListings();
            logService.publish("Successfully deleted old House Listings", LoggingLevel.INFO);
            jobStatus = JobStatus.SUCCESS;
        } catch (Exception e) {
            logService.publish("Failed to delete old House Listings\n" + e, LoggingLevel.ERROR);
        } finally {
            publisher.fireAsync(generateJobEvent(DELETE_HOUSING_LISTINGS_JOB, jobStatus));
        }
    }

    public void runTriggerRegistrationRotationJob() {
        logService.publish("Running Cron Job, triggering registration rotation", LoggingLevel.INFO);
        keymasterWebhookService.triggerRegistrationKeyRotation(
                new RotatingKeyEvent("Requesting new service registration key for Keymaster Service", LocalDateTime.now())
        );
        publisher.fireAsync(generateJobEvent(TRIGGER_ROTATION_JOB, JobStatus.TRIGGERED));
    }

    // ========== HELPER LOGIC ==========
    private void executeImageDeletion(String jobName, ImageDeletionTask task) {
        JobStatus jobStatus = JobStatus.FAILED;
        try {
            task.run();
            logService.publish("Successfully deleted images for " + jobName, LoggingLevel.INFO);
            jobStatus = JobStatus.SUCCESS;
        } catch (JsonProcessingException e) {
            logService.publish("Failed to delete images for " + jobName + "\n" + e, LoggingLevel.ERROR);
        } finally {
            publisher.fireAsync(generateJobEvent(jobName, jobStatus));
        }
    }

    private JobEvent generateJobEvent(String jobName, JobStatus jobStatus) {
        long id = Instant.now().toEpochMilli();
        return new JobEvent(id, jobName, jobStatus, LocalDateTime.now());
    }

    @FunctionalInterface
    private interface ImageDeletionTask {
        void run() throws JsonProcessingException;
    }
}