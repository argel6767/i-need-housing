package com.ineedhousing.backend.cron_job_service.ws;

import com.ineedhousing.backend.cron_job_service.model.ClearSavedLogsEvent;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.model.PublishedParsedLog;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CronJobLogStreamManager {

    private final CronJobLogStreamClient cronJobLogStreamClient;
    private final CronJobLogStreamService cronJobLogStreamService;


    public CronJobLogStreamManager(CronJobLogStreamClient cronJobLogStreamClient, CronJobLogStreamService cronJobLogStreamService) {
        this.cronJobLogStreamClient = cronJobLogStreamClient;
        this.cronJobLogStreamService = cronJobLogStreamService;
    }

    public void startLogStream() {
        cronJobLogStreamClient.connect();
    }

    public void stopLogStream() {
        cronJobLogStreamClient.close();
    }

    @EventListener
    public void onLogReceived(PublishedParsedLog parsedLog) {
        cronJobLogStreamService.addLog(parsedLog.log());
    }

    @EventListener
    public void onLogStreamClosed(ClearSavedLogsEvent clearLogs) {
        cronJobLogStreamService.clearSavedLogs();
    }

    public LogEventResponse getLogs() {
        return new LogEventResponse(cronJobLogStreamService.getLogs());
    }
}
