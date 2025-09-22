package com.ineedhousing.backend.admin.views;

import com.ineedhousing.backend.admin.components.Navigation;
import com.ineedhousing.backend.constants.Service;
import com.ineedhousing.backend.cron_job_service.model.LogEventResponse;
import com.ineedhousing.backend.cron_job_service.rest.CronJobRestService;
import com.ineedhousing.backend.ws.v1.ServiceLogStreamManager;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.springframework.context.annotation.Scope;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.ineedhousing.backend.admin.components.GridCreator.buildLogEventsGrid;

@Route("/admin/cron-job-service")
@PageTitle("Cron Job Service")
@org.springframework.stereotype.Component
@Scope("prototype")
public class CronJobServiceView extends VerticalLayout {

    private Consumer<LogEventResponse.LogEvent> logListener;
    private final ServiceLogStreamManager serviceLogStreamManager;
    private final CronJobRestService cronJobRestService;
    private List<LogEventResponse.LogEvent> recentLogs = new ArrayList<>();
    //private String healthPing = new HealthPing("", new ArrayList<>());
    private final Grid<LogEventResponse.LogEvent> logGrid = buildLogEventsGrid(recentLogs);
    private boolean isLiveStreaming = false;
    private final Button viewLiveButton = new Button("View Live Logs");
    private Registration viewLiveButtonRegistration; // Track the current click listener

    public CronJobServiceView(ServiceLogStreamManager serviceLogStreamManager, CronJobRestService cronJobRestService) {
        this.serviceLogStreamManager = serviceLogStreamManager;
        this.cronJobRestService = cronJobRestService;
        setSizeFull();
        add(new H1("Cron Job Service Overview"));
        add(Navigation.getHorizontalNav());
        add(createLogViewer());
    }



    public Div createLogViewer() {
        LogEventResponse logs = cronJobRestService.getMostRecentLogs(10);
        recentLogs.addAll(logs.logs());
        Div card = new Div();
        card.add(new H3("Most recent logs"));
        card.add(logGrid);
        H4 h4 = new H4("Request more logs");
        Button last25 = new Button("View Last 25 Logs");
        last25.addClickListener(event -> fetchMostRecentLogs(25));
        Button last50 = new Button("View Last 50 Logs");
        last50.addClickListener(event -> fetchMostRecentLogs(50));
        Button last100 = new Button("View Last 100 Logs");
        last100.addClickListener(event -> {
            if (isLiveStreaming) {
                serviceLogStreamManager.stopLogStream();
            }
            recentLogs.clear();
            recentLogs.addAll(cronJobRestService.getMostRecentLogs().logs());
            logGrid.getDataProvider().refreshAll();
        });
        setupViewLiveButton();
        HorizontalLayout options = new HorizontalLayout(last25, last50, last100, viewLiveButton);
        card.add(h4, options);
        return card;
    }

    private void setupViewLiveButton() {
        // Remove any existing listener before adding a new one
        if (viewLiveButtonRegistration != null) {
            viewLiveButtonRegistration.remove();
        }

        if (!isLiveStreaming) {
            viewLiveButton.setText("View Live Logs");
            viewLiveButton.getStyle().remove("background-color");
            viewLiveButtonRegistration = viewLiveButton.addClickListener(event -> {
                try {
                    startLiveStreaming();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            viewLiveButton.setText("Stop Live Logs");
            viewLiveButton.getStyle().set("background-color", "var(--lumo-error-color)");
            viewLiveButtonRegistration = viewLiveButton.addClickListener(event -> stopLiveStreaming());
        }
    }

    private void updateLogGrid() {
        recentLogs = serviceLogStreamManager.getLogs().logs();
        logGrid.getDataProvider().refreshAll();
    }

    private void fetchMostRecentLogs(int limit) {
        if (isLiveStreaming) {
            serviceLogStreamManager.stopLogStream();
        }
        recentLogs.clear();
        recentLogs.addAll(cronJobRestService.getMostRecentLogs(limit).logs());
        logGrid.getDataProvider().refreshAll();
    }

    private void startLiveStreaming() throws URISyntaxException {
        serviceLogStreamManager.startLogStream(Service.CRON_JOB_SERVICE);
        recentLogs.clear();
        isLiveStreaming = true;
        setupViewLiveButton();

        logListener = this::onLogReceived;
        serviceLogStreamManager.addLogListener(logListener);

        // Get initial logs from the stream
        recentLogs.addAll(serviceLogStreamManager.getLogs().logs());
        updateLogGrid();
    }

    private void stopLiveStreaming() {
        serviceLogStreamManager.stopLogStream();
        isLiveStreaming = false;
        setupViewLiveButton();
        if (logListener != null) {
            serviceLogStreamManager.removeLogListener(logListener);
            logListener = null;
        }
    }


    private void onLogReceived(LogEventResponse.LogEvent log) {
        if (isLiveStreaming) {
            // Use UI.access() to safely update UI from background thread
            getUI().ifPresent(ui -> ui.access(() -> {
                // Get updated logs from the stream manager
                recentLogs = serviceLogStreamManager.getLogs().logs();
                updateLogGrid();
            }));
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Clean up when view is closed
        if (isLiveStreaming) {
            stopLiveStreaming();
        }
        if (viewLiveButtonRegistration != null) {
            viewLiveButtonRegistration.remove();
            viewLiveButtonRegistration = null;
        }
        super.onDetach(detachEvent);
    }
}
