package ineedhousing.cronjob.azure.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.models.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.NewListingsEventPublisher;
import ineedhousing.cronjob.new_listings_service.models.CityDto;
import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class DatabaseService {

    @Inject
    AgroalDataSource dataSource;

    @Inject
    LogService logService;

    @Inject
    NewListingsEventPublisher newListingsEventPublisher;

    /**
     * Deletes HouseListings that are older than 6 months in the INeedHousing DB
     */
    public void deleteOldListings() {
        logService.publish("Deleting old listings", LoggingLevel.INFO);

        String sql = """
            DELETE FROM housing_listing
            WHERE created_at IS NULL OR
            created_at < NOW() - INTERVAL '6 months';
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int rowsDeleted = ps.executeUpdate();
            logService.publish("Deleted " + rowsDeleted + " old listings", LoggingLevel.INFO);
            logService.publish("Triggering new-listings-service webhook", LoggingLevel.INFO);
            newListingsEventPublisher.publishNewListingEvent("Webhook event created, " + rowsDeleted + " total listings deleted");
        } catch (Exception e) {
            Log.error("Error deleting old listings", e);
            logService.publish("Error deleting old listings\nError message:" + e.getMessage(), LoggingLevel.ERROR);
        }
    }

    public List<CityDto> fetchAllCities() {
        logService.publish("Fetching all cities", LoggingLevel.INFO);

        String sql = """
            SELECT city_name, latitude, longitude FROM cities;
        """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet results = preparedStatement.executeQuery();
            List<CityDto> cityDtoList = new ArrayList<>();

            while (results.next()) {
                String cityName = results.getString("city_name");
                Double latitude = results.getDouble("latitude");
                Double longitude = results.getDouble("longitude");
                cityDtoList.add(new CityDto(cityName, latitude, longitude));
            }
            return cityDtoList;
        }
        catch (Exception e) {
            logService.publish("Error fetch cities\nError message" + e.getMessage(), LoggingLevel.ERROR);
            return List.of();
        }
    }
}
