package com.ineedhousing.configs;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExecutorConfiguration {

    private final ExecutorService virtualThreadExecutor =
            Executors.newVirtualThreadPerTaskExecutor();

    @Produces
    @ApplicationScoped
    public ExecutorService virtualThreadExecutor() {
        return virtualThreadExecutor;
    }

    @PreDestroy
    void shutdown() {
        virtualThreadExecutor.shutdown();
    }
}
