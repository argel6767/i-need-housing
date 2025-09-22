package com.ineedhousing.backend.ws.v1;

import com.ineedhousing.backend.cron_job_service.model.ClearSavedLogsEvent;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.model.PublishedParsedLog;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Log
@Service
public class ServiceLogStreamManager {

    private final ServiceStreamClientFactory clientFactory;
    private final ServiceLogStreamService serviceLogStreamService;
    private volatile WebSocketClient currentClient;
    private final List<Consumer<LogEventResponse.LogEvent>> listeners = new ArrayList<>();


    public ServiceLogStreamManager(ServiceStreamClientFactory clientFactory, ServiceLogStreamService serviceLogStreamService) {
        this.clientFactory = clientFactory;
        this.serviceLogStreamService = serviceLogStreamService;
    }

    public void startLogStream(com.ineedhousing.backend.constants.Service service) throws URISyntaxException {
        if (currentClient != null) {
            stopLogStream();
        }
        currentClient = clientFactory.createClient(service);
        currentClient.connect();
    }

    public void stopLogStream() {
        if (currentClient == null) {
            return;
        }
        currentClient.close();
        currentClient = null;
    }

    public void addLogListener(Consumer<LogEventResponse.LogEvent> listener) {
        listeners.add(listener);
    }

    public void removeLogListener(Consumer<LogEventResponse.LogEvent> listener) {
        listeners.remove(listener);
    }

    @EventListener
    public void onLogReceived(PublishedParsedLog parsedLog) {
        // Notify all registered listeners
        listeners.forEach(listener -> {
            try {
                listener.accept(parsedLog.log());
            } catch (Exception e) {
                log.warning("Error notifying log listener: " + e.getMessage());
            }
        });
    }

    @EventListener
    public void onLogStreamClosed(ClearSavedLogsEvent clearLogs) {
        serviceLogStreamService.clearSavedLogs();
    }

    public LogEventResponse getLogs() {
        return new LogEventResponse(serviceLogStreamService.getLogs());
    }

}
