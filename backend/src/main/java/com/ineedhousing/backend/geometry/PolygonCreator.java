package com.ineedhousing.backend.geometry;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

/**
 * Houses the logic for creating Polygons
 */
public class PolygonCreator {

    /**
     * creates a close approximation of a circle, dependent on number of sides
     * the more side -> the more accurate
     * @param center
     * @param radius
     * @param numSides
     * @return
     */
    public static Polygon createCircle(Point center, int radius, int numSides) {
        GeometricShapeFactory factory = new GeometricShapeFactory();
        factory.setNumPoints(numSides);
        factory.setCentre(center.getCoordinate());
        
        //adjusting done to the radius to be in degrees and also being accurate due to earth curvature
        double radiusInDegrees = radius / 69.0;
        double latitudeCorrection = Math.cos(Math.toRadians(center.getY()));
        double adjustedRadiusInDegrees = radiusInDegrees / latitudeCorrection;

        factory.setSize(adjustedRadiusInDegrees*2);
        Polygon circle = factory.createCircle();
        circle.setSRID(center.getSRID());
        return circle;
    }
}
