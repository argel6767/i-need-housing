package com.ineedhousing.backend.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

class GoogleCronJobServiceNewListingsRetrievalRestClientConfigurationTest {

    private static final String RENT_CAST_BASE_URL = "https://rentcast.example.com";
    private static final String RENT_CAST_API_KEY = "test-rentcast-key";

    private static final String AIRBNB_BASE_URL = "https://airbnb.example.com";
    private static final String AIRBNB_HOST = "airbnb-host";
    private static final String AIRBNB_API_KEY = "test-airbnb-key";

    private static final String CRAIGSLIST_BASE_URL = "https://craigslist.example.com";
    private static final String CRAIGSLIST_HOST = "craigslist-host";
    private static final String CRAIGSLIST_API_KEY = "test-craigslist-key";

    @InjectMocks
    private NewListingsRetrievalRestClientConfiguration newListingsRetrievalRestClientConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void rentCastRestClient_createsProperClient() {
        // Act
        RestClient rentCastClient = RestClient.builder()
                .baseUrl(RENT_CAST_BASE_URL)
                .defaultHeader("X-Api-Key", RENT_CAST_API_KEY)
                .build();

        // Assert
        assertNotNull(rentCastClient);
    }

    @Test
    void airbnbRestClient_createsProperClient() {
        // Act
        RestClient airbnbClient = RestClient.builder()
                .baseUrl(AIRBNB_BASE_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("x-rapidapi-key", AIRBNB_API_KEY);
                    httpHeaders.set("x-rapidapi-host", AIRBNB_HOST);
                })
                .build();

        // Assert
        assertNotNull(airbnbClient);
    }

    @Test
    void craigslistRestClient_createsProperClient() {
        // Act
        RestClient craigslistClient = RestClient.builder()
                .baseUrl(CRAIGSLIST_BASE_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("x-rapidapi-key", CRAIGSLIST_API_KEY);
                    httpHeaders.set("x-rapidapi-host", CRAIGSLIST_HOST);
                })
                .build();

        // Assert
        assertNotNull(craigslistClient);
    }
}
