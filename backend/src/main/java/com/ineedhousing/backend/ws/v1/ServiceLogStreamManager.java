package com.ineedhousing.backend.ws.v1;

import com.ineedhousing.backend.cron_job_service.models.ClearSavedLogsEvent;
import com.ineedhousing.backend.cron_job_service.models.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.models.PublishedParsedLog;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Log
@Service
public class ServiceLogStreamManager {

    private final ServiceStreamClientFactory clientFactory;
    private final ServiceLogStreamService serviceLogStreamService;
    private final Map<com.ineedhousing.backend.constants.Service, WebSocketClient> activeClients = new ConcurrentHashMap<>();
    private final List<Consumer<LogEventResponse.LogEvent>> listeners = new ArrayList<>();

    public ServiceLogStreamManager(ServiceStreamClientFactory factory, ServiceLogStreamService serviceLogStreamService) {
        this.clientFactory = factory;
        this.serviceLogStreamService = serviceLogStreamService;
    }

    public synchronized void startLogStream(com.ineedhousing.backend.constants.Service service) throws URISyntaxException {
        // Already connected? Don’t reconnect
        if (activeClients.containsKey(service)) return;

        ServiceLogStreamClient client = clientFactory.createClient(service);
        activeClients.put(service, client);
        client.connect();
    }

    public synchronized void stopLogStream(com.ineedhousing.backend.constants.Service service) {
        WebSocketClient client = activeClients.remove(service);
        if (client != null) {
            client.close();
        }
    }

    public synchronized void stopAllLogStreams() {
        activeClients.values().forEach(WebSocketClient::close);
        activeClients.clear();
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
