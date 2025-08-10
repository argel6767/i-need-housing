package ineedhousing.cronjob.azure.postgres;

import ineedhousing.cronjob.log.LogService;
import ineedhousing.cronjob.log.model.LoggingLevel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.agroal.api.AgroalDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@ApplicationScoped
public class DatabaseService {

    @Inject
    AgroalDataSource dataSource;

    @Inject
    LogService logService;

    /**
     * Deletes HouseListings that are older than 6 months in the INeedHousing DB
     */
    public void deleteOldListings() {
        Log.info("Deleting old listings");

        String sql = """
            DELETE FROM house_listings
            WHERE created_date < NOW() - INTERVAL '6 months'
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int rowsDeleted = ps.executeUpdate();
            logService.publish("Deleted " + rowsDeleted + " old listings", LoggingLevel.INFO);
        } catch (Exception e) {
            Log.error("Error deleting old listings", e);
            logService.publish("Error deleting old listings\n" + e, LoggingLevel.ERROR);
            throw new RuntimeException(e);
        }
    }
}
