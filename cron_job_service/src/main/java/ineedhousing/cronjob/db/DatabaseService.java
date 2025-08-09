package ineedhousing.cronjob.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.logging.Log;

@ApplicationScoped
public class DatabaseService {

    @Inject
    EntityManager em;

    /**
     * Deletes HouseListings that are older than 4 months old in the INeedHousing DB
     */
    @Transactional
    public void deleteOldListings() {
        Log.info("Deleting old listings");
        em.createNativeQuery("DELETE FROM house_listings WHERE created_date < NOW() - INTERVAL '6 months'")
                .executeUpdate();
    }
}
