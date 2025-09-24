package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.cron.models.JobEvent;
import ineedhousing.cronjob.cron.models.JobStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/jobs")
public class JobResource {

    @Inject
    JobService jobService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JobEvent> getJobs(@QueryParam("jobStatus") JobStatus jobStatus, @QueryParam("quantity") @DefaultValue("20") Integer quantity) {
        if (jobStatus == null) {
            return jobService.getJobs(quantity);
        }
        return jobService.getJobs(jobStatus, quantity);
    }
}
