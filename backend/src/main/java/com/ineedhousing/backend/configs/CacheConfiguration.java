package com.ineedhousing.backend.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    private final int MAXIMUM_CACHE_SIZE = 200;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Signed URLs - 15 minutes
        cacheManager.registerCustomCache("signed-urls",
                Caffeine.newBuilder()
                        .expireAfterWrite(15, TimeUnit.MINUTES)
                        .maximumSize(MAXIMUM_CACHE_SIZE)
                        .build());

        // User profiles - 3 hour
        cacheManager.registerCustomCache("users",
                Caffeine.newBuilder()
                        .expireAfterWrite(3, TimeUnit.HOURS)
                        .maximumSize(MAXIMUM_CACHE_SIZE)
                        .build());

        // User profiles - 1 hour
        cacheManager.registerCustomCache("preferences",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(MAXIMUM_CACHE_SIZE)
                        .build());

        cacheManager.registerCustomCache("listings",
                Caffeine.newBuilder()
                        .expireAfterWrite(8, TimeUnit.HOURS)
                        .maximumSize(MAXIMUM_CACHE_SIZE)
                        .build());

        return cacheManager;
    }
}
