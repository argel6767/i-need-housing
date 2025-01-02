package com.ineedhousing.backend.user_search_preferences.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.ineedhousing.backend.user_search_preferences.UserPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Builder Class for a UserPreference object
 * allows for chain calling and choosing what to/to not add
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceBuilder {
    
    private UserPreference userPreference = new UserPreference();

    public UserPreferenceBuilder addJobLocation(Point jobLocation) {
        userPreference.setJobLocation(jobLocation);
        return this;
    }

    public UserPreferenceBuilder addCityOfEmployment(Point cityPoint) {
        userPreference.setCityOfEmployment(cityPoint);
        return this;
    }

    public UserPreferenceBuilder addDesiredArea(Point center, Integer radius, int numOfSides) {
        Polygon area;
        if (radius != null) {
            area = PolygonCreator.createCircle(center, radius, numOfSides);
        }
        else {
            area = PolygonCreator.createCircle(center, userPreference.getMaxRadius(), numOfSides);
        }
        userPreference.setDesiredArea(area);
        return this;
    }

    public UserPreferenceBuilder addMaxRadius(int maxRadius) {
        userPreference.setMaxRadius(maxRadius);
        return this;
    }

    public UserPreferenceBuilder addMaxRent(int maxRent) {
        userPreference.setMaxRent(maxRent);
        return this;
    }

    // will be used for future implementation
    public UserPreferenceBuilder addTravelType(String travelType) {
        userPreference.setTravelType(travelType);
        return this;
    }

    public UserPreferenceBuilder addMinNumberOfBedrooms(int numOfBedrooms) {
        userPreference.setMinNumberOfBedRooms(numOfBedrooms);
        return this;
    }

    public UserPreferenceBuilder addMinNumberOfBathrooms(double numOfBathrooms) {
        userPreference.setMinNumberOfBathrooms(numOfBathrooms);
        return this;
    }

    public UserPreferenceBuilder addIsFurnished(boolean isFurnished) {
        userPreference.setIsFurnished(isFurnished);
        return this;
    }

    public UserPreferenceBuilder addInternshipStart(LocalDate startDate) {
        userPreference.setInternshipStart(startDate);
        return this;
    }

    public UserPreferenceBuilder addInternshipEnd(LocalDate endDate) {
        userPreference.setInternshipEnd(endDate);
        return this;
    }

    public UserPreference build() {
        userPreference.setUpdatedAt(LocalDateTime.now());
        return userPreference;
    }
}
