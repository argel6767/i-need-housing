package com.ineedhousing.backend.apis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RentCastApiService {
    private final RestClient restClient;

    public RentCastApiService (@Qualifier("RentCast API") RestClient restClient) {
        this.restClient = restClient;
    }
    
}
