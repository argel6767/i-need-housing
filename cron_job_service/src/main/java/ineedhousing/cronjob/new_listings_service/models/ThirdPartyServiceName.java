package ineedhousing.cronjob.new_listings_service.models;

import java.util.List;

public enum ThirdPartyServiceName {
    Zillow, Airbnb, RentCast;

    public static List<ThirdPartyServiceName> all() {
        return List.of(ThirdPartyServiceName.values());
    }
}
