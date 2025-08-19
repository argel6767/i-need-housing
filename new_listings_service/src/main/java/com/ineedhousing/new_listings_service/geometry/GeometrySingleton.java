package com.ineedhousing.new_listings_service.geometry;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Singleton Pattern for a GeometryFactory that is shared among the external Api services
 * the factory will be used to make Point object
 * @return
 */
public class GeometrySingleton {
    private static GeometryFactory factory = null;

    private GeometrySingleton(){};

    /**
     * static getInstance(), synchronized to make sure threads don't make multiple
     * id factory is null (hasn't been made yet) it is then instantiated first and then returned
     * @return factory
     */
    public static synchronized GeometryFactory getInstance() {
        if (factory == null) {
            factory = new GeometryFactory(new PrecisionModel(), 4326);
        }
        return factory;
    }
}