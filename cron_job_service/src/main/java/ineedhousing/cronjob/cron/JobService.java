package ineedhousing.cronjob.cron;

import ineedhousing.cronjob.cron.models.JobEvent;
import ineedhousing.cronjob.cron.models.JobStatus;
import ineedhousing.cronjob.log.models.CircularBuffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;

import java.util.List;

@ApplicationScoped
public class JobService {

    private final CircularBuffer<JobEvent> jobs = new CircularBuffer<>(20);

    void onJobEvent(@ObservesAsync JobEvent jobEvent) {
        jobs.add(jobEvent);
    }

    public List<JobEvent> getJobs(int numberOfJobs) {
        return jobs.getMostRecentEntries(numberOfJobs);
    }

    public List<JobEvent> getJobs(JobStatus jobStatus, int numberOfJobs) {
        return jobs.getMostRecentEntries(numberOfJobs)
                .stream()
                .filter(jobEvent -> jobEvent.status().equals(jobStatus))
                .toList();
    }

}
