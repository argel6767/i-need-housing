package com.ineedhousing.new_listings_service.constants;

import com.ineedhousing.new_listings_service.models.CityCoordinates;
import java.util.List;

public class LocationCoordinates {

    public static final CityCoordinates SEATTLE_COORDINATES = new CityCoordinates("Seattle", 47.6061, -122.3328);

    public static final CityCoordinates SAN_FRANCISCO_COORDINATES = new CityCoordinates("San Francisco",37.7749, 122.4194);

    public static final CityCoordinates LOS_ANGELES_COORDINATES = new CityCoordinates("Los Angeles", 34.0549, -118.2426);

    public static final CityCoordinates DALLAS_COORDINATES = new CityCoordinates("Dallas",32.7767, -96.7970);

    public static final CityCoordinates CHICAGO_COORDINATES = new CityCoordinates("Chicago",41.8832, -87.6324);

    public static final CityCoordinates NEW_YORK_CITY_COORDINATES = new CityCoordinates("New York",40.7128, -74.0060);

    public static final CityCoordinates BOSTON_COORDINATES = new CityCoordinates("Boston",42.3555, -71.0565);

    public static List<CityCoordinates> getCityCoordinates() {
        return List.of(SEATTLE_COORDINATES, SAN_FRANCISCO_COORDINATES, LOS_ANGELES_COORDINATES, DALLAS_COORDINATES, CHICAGO_COORDINATES, NEW_YORK_CITY_COORDINATES, BOSTON_COORDINATES);
    }
}
