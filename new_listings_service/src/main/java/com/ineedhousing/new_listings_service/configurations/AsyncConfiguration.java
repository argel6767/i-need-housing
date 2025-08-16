package com.ineedhousing.new_listings_service.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean("virtualThreadTaskExecutor")
    public TaskExecutor taskExecutor() {
        return new TaskExecutor() {
            private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            @Override
            public void execute(Runnable task) {
                executor.submit(task);
            }
        };
    }
}

