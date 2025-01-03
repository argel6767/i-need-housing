package com.ineedhousing.backend.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.ineedhousing.backend.geometry.PolygonCreator;

import org.locationtech.jts.geom.GeometryFactory;

class PolygonCreatorTest {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    void testCreateCircle_NotNull() {
        Point center = geometryFactory.createPoint(new Coordinate(0, 0));
        int radius = 10;
        int numSides = 36;

        Polygon circle = PolygonCreator.createCircle(center, radius, numSides);

        assertNotNull(circle, "The created polygon should not be null");
    }

    @Test
    void testCreateCircle_CorrectNumberOfSides() {
        Point center = geometryFactory.createPoint(new Coordinate(0, 0));
        int radius = 10;
        int numSides = 36;

        Polygon circle = PolygonCreator.createCircle(center, radius, numSides);

        // Subtract 1 to account for the closed polygon (first and last point are the same)
        int actualNumSides = circle.getCoordinates().length - 1;
        assertEquals(numSides, actualNumSides, "The polygon should have the correct number of sides");
    }

    @Test
    void testCreateCircle_CorrectCentroid() {
        Point center = geometryFactory.createPoint(new Coordinate(5, 5));
        int radius = 10;
        int numSides = 36;

        Polygon circle = PolygonCreator.createCircle(center, radius, numSides);

        Point centroid = circle.getCentroid();
        assertEquals(center.getX(), centroid.getX(), 0.0001, "The centroid's X coordinate should match the center");
        assertEquals(center.getY(), centroid.getY(), 0.0001, "The centroid's Y coordinate should match the center");
    }

    @Test
    void testCreateCircle_CorrectRadius() {
        Point center = geometryFactory.createPoint(new Coordinate(0, 0));
        int radius = 10;
        int numSides = 36;

        Polygon circle = PolygonCreator.createCircle(center, radius, numSides);

        // Calculate the approximate radius from the first vertex
        double calculatedRadius = circle.getCoordinates()[0].distance(center.getCoordinate());
        assertEquals(radius, calculatedRadius, 0.5, "The calculated radius should be close to the expected radius");
    }
}

