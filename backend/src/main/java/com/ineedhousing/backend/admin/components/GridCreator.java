package com.ineedhousing.backend.admin.components;

import java.util.List;

import com.ineedhousing.backend.cron_job_service.models.LogEventResponse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.vaadin.flow.component.grid.Grid;

public class GridCreator {

    public static Grid<User> buildUserGrid(List<User> users) {
        Grid<User> grid = new Grid<>(User.class, false);
            grid.addColumn(User::getEmail).setHeader("Email");
            grid.addColumn(User::getAuthorities).setHeader("Authorities");
            grid.addColumn(User::getLastLogin).setHeader("Last Logged In");
            grid.addColumn(User::getCreatedAt).setHeader("Created At");
            grid.addColumn(User::getIsEnabled).setHeader("Is Verified");
            grid.setItems(users);
            grid.setWidth("100%");
            return grid;
    }

    public static Grid<HousingListing> buildListingsGrid(List<HousingListing> listings) {
        Grid<HousingListing> grid = new Grid<>(HousingListing.class);
            grid.removeColumnByKey("imageUrls");
            grid.removeColumnByKey("coordinates");
            grid.removeColumnByKey("location");
            grid.removeColumnByKey("id");
            grid.removeColumnByKey("isFurnished");
            grid.removeColumnByKey("isPetFriendly");
            grid.addColumn(housing -> {
                Point location = housing.getLocation();
                Coordinate coordinates = location.getCoordinate();
                return String.format("{%.2f, %.2f}", coordinates.getX(), coordinates.getY());
            }).setHeader("Coordinates");
            grid.setItems(listings);
            grid.setWidth("100%");
            return grid;
    }

    public static Grid<LogEventResponse.LogEvent> buildLogEventsGrid(List<LogEventResponse.LogEvent> logs) {
        Grid<LogEventResponse.LogEvent> grid = new Grid<>(LogEventResponse.LogEvent.class);
        grid.setItems(logs);
        grid.setWidth("100%");
        return grid;
    }
}
