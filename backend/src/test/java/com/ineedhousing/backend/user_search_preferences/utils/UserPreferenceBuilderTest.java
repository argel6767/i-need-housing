package com.ineedhousing.backend.user_search_preferences.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.ineedhousing.backend.user_search_preferences.UserPreference;

class UserPreferenceBuilderTest {

    private GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    void testAddJobLocation() {
        Point jobLocation = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(10, 20));
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addJobLocation(jobLocation)
                .build();

        assertEquals(jobLocation, userPreference.getJobLocation());
    }

    @Test
    void testAddCityOfEmployment() {
        Point cityPoint = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(30, 40));
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addCityOfEmployment(cityPoint)
                .build();

        assertEquals(cityPoint, userPreference.getCityOfEmployment());
    }

    @Test
    void testAddMaxRadius() {
        int maxRadius = 50;
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addMaxRadius(maxRadius)
                .build();

        assertEquals(maxRadius, userPreference.getMaxRadius());
    }

    @Test
    void testAddDesiredArea() {
        Point center = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(50, 50));
        int radius = 10;
        int numOfSides = 32;

        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addDesiredArea(center, radius, numOfSides)
                .build();

        Polygon desiredArea = userPreference.getDesiredArea();

        // Ensure the polygon is not null
        assertNotNull(desiredArea);

        // Ensure the center of the polygon matches the center point
        assertEquals(center.getX(), desiredArea.getCentroid().getX(), 0.01);
        assertEquals(center.getY(), desiredArea.getCentroid().getY(), 0.01);

        // Ensure the number of points in the polygon matches the number of sides specified
        assertEquals(numOfSides + 1, desiredArea.getCoordinates().length); // +1 because it is a closed polygon
    }

    @Test
    void testAddDesiredAreaWithNullCenter() {
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addDesiredArea(null, 10, 32)
                .build();

        assertNull(userPreference.getDesiredArea());
    }

    @Test
    void testAddMaxRent() {
        int maxRent = 2000;
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addMaxRent(maxRent)
                .build();

        assertEquals(maxRent, userPreference.getMaxRent());
    }

    @Test
    void testAddTravelType() {
        String travelType = "Car";
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addTravelType(travelType)
                .build();

        assertEquals(travelType, userPreference.getTravelType());
    }

    @Test
    void testAddMinNumberOfBedrooms() {
        int numOfBedrooms = 2;
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addMinNumberOfBedrooms(numOfBedrooms)
                .build();

        assertEquals(numOfBedrooms, userPreference.getMinNumberOfBedRooms());
    }

    @Test
    void testAddMinNumberOfBathrooms() {
        double numOfBathrooms = 1.5;
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addMinNumberOfBathrooms(numOfBathrooms)
                .build();

        assertEquals(numOfBathrooms, userPreference.getMinNumberOfBathrooms());
    }

    @Test
    void testAddIsFurnished() {
        boolean isFurnished = true;
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addIsFurnished(isFurnished)
                .build();

        assertEquals(isFurnished, userPreference.getIsFurnished());
    }

    @Test
    void testAddInternshipStart() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addInternshipStart(startDate)
                .build();

        assertEquals(startDate, userPreference.getInternshipStart());
    }

    @Test
    void testAddInternshipEnd() {
        LocalDate endDate = LocalDate.of(2025, 6, 30);
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference())
                .addInternshipEnd(endDate)
                .build();

        assertEquals(endDate, userPreference.getInternshipEnd());
    }

    @Test
    void testBuildUpdatesTimestamp() {
        UserPreference userPreference = new UserPreferenceBuilder(new UserPreference()).build();
        LocalDateTime updatedAt = userPreference.getUpdatedAt();

        assertNotNull(updatedAt);
        assertTrue(updatedAt.isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
