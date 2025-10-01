package com.ineedhousing.new_listings_service.models.responses;

import com.ineedhousing.new_listings_service.models.data.City;

public record CityDto(String cityName, Double latitude, Double longitude) {

    public static CityDto createDto(City city) {
        return new CityDto(city.getCityName(), city.getLatitude(), city.getLongitude());
    }
}
