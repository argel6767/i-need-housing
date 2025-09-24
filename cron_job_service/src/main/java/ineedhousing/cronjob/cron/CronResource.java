package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.configs.qualifiers.VirtualThreadPool;
import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Path("/cron-jobs")
public class CronResource {

    @Inject
    CronService cronService;

    @Inject
    LogService logService;

    @Inject
    @VirtualThreadPool
    ExecutorService virtualThreadExecutor;

    @POST
    @Path("/images/i-need-housing")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteOldINeedHousingImagesJob() {
        triggerJobAsync(cronService::runDeleteOldINeedHousingImagesJob, "deleteOldINeedHousingImagesJob");
        return formatReturnMessage("deleteOldINeedHousingImagesJob");
    }

    @POST
    @Path("/images/cron-job")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteOldCronJobServiceImagesJob() {
        triggerJobAsync(cronService::runDeleteOldCronJobServiceImagesJob, "deleteOldCronJobServiceImagesJob");
        return formatReturnMessage("deleteOldCronJobServiceImagesJob");
    }

    @POST
    @Path("/images/new-listings")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteNewListingsServiceImagesJob() {
        triggerJobAsync(cronService::runDeleteNewListingsServiceImagesJob, "deleteNewListingsServiceImagesJob");
        return formatReturnMessage("deleteNewListingsServiceImagesJob");
    }

    @POST
    @Path("/listings")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteOldHousingListingsJob() {
        triggerJobAsync(cronService::runDeleteOldHousingListingsJob, "deleteOldHousingListingsJob");
        return formatReturnMessage("deleteOldHousingListingsJob");
    }

    @POST
    @Path("/triggers/key-rotation")
    @Produces(MediaType.TEXT_PLAIN)
    public String triggerRegistrationRotationJob() {
        triggerJobAsync(cronService::runTriggerRegistrationRotationJob, "triggerRegistrationRotationJob");
        return formatReturnMessage("triggerRegistrationRotationJob");
    }

    // ========== Helper methods ==========
    private void triggerJobAsync(Runnable job, String jobName) {
        logService.publish(
                String.format("Triggering %s from manual endpoint request asynchronously", jobName),
                LoggingLevel.INFO
        );

        // Run in executor but fully inside a lambda, not just method ref
        CompletableFuture.runAsync(() -> {
            try {
                job.run();
            } catch (Exception e) {
                logService.publish(
                        String.format("Error while running %s: %s", jobName, e),
                        LoggingLevel.ERROR
                );
            }
        }, virtualThreadExecutor);
    }

    private String formatReturnMessage(String job) {
        return String.format("%s triggered, check logs for details", job);
    }
}