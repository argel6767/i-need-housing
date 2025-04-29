package com.ineedhousing.backend;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.metrics.buffering.StartupTimeline;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;


@Component
public class StartupMetricsLogger implements ApplicationListener<ApplicationReadyEvent> {

@Autowired
private BufferingApplicationStartup applicationStartup;

@Override
public void onApplicationEvent(ApplicationReadyEvent event) {
    var timeline = applicationStartup.drainBufferedTimeline();
    
    System.out.println("========== TOP 20 SLOWEST STARTUP STEPS ==========");
    
    // Sort by duration (longest first) and print the top 20
    timeline.getEvents().stream()
        .sorted((a, b) -> Long.compare(b.getDuration().toMillis(), a.getDuration().toMillis()))
        .limit(20)
        .forEach(startupEvent -> {
            System.out.printf("Step: %s - Duration: %d ms%n", 
                startupEvent.getStartupStep().getName(), 
                startupEvent.getDuration().toMillis());
        });
        
    System.out.println("=================================================");
}
}
