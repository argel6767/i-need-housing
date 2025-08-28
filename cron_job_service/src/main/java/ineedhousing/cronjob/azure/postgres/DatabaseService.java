package ineedhousing.cronjob.azure.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import ineedhousing.cronjob.new_listings_service.NewListingsEventPublisher;
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
        Log.info("Deleting old listings");

        String sql = """
            DELETE FROM house_listings
            WHERE created_at IS NULL OR 
            created_at < NOW() - INTERVAL '6 months';
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int rowsDeleted = ps.executeUpdate();
            logService.publish("Deleted " + rowsDeleted + " old listings", LoggingLevel.INFO);

            if (rowsDeleted > 5000) {
                logService.publish("Triggering new-listings-service webhook", LoggingLevel.INFO);
                newListingsEventPublisher.publishNewListingEvent("Webhook event created, " + rowsDeleted + " total listings deleted");
            }
        } catch (Exception e) {
            Log.error("Error deleting old listings", e);
            logService.publish("Error deleting old listings\n" + e, LoggingLevel.ERROR);
            throw new RuntimeException(e);
        }
    }
}
