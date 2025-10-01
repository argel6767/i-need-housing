package ineedhousing.cronjob.new_listings_service.models;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CityDto(String cityName, Double latitude, Double longitude) {
}
