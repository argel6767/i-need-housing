package com.ineedhousing.backend.cron_job_service.rest;

import com.ineedhousing.backend.cron_job_service.models.JobEvent;
import com.ineedhousing.backend.cron_job_service.models.JobStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cron-jobs")
public class CronJobController {

    private final CronJobRestService cronJobRestService;

    public CronJobController(CronJobRestService cronJobRestService) {
        this.cronJobRestService = cronJobRestService;
    }

    @GetMapping("/jobs")
    public List<JobEvent> getJobs(@RequestParam("jobStatus") JobStatus jobStatus, @RequestParam("quantity") Integer quantity) {
        return cronJobRestService.getJobEvents(jobStatus, quantity);
    }

    @PostMapping("/jobs/{jobName}")
    public void triggerJob(@PathVariable("jobName") String jobName) {
        cronJobRestService.triggerJob(jobName);
    }
}
