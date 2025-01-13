package com.ineedhousing.backend.housing_listings;

import com.ineedhousing.backend.user_search_preferences.UserPreference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserPreferencesFiltererTest {

    private UserPreferencesFilterer filterer = new UserPreferencesFilterer();
    static List<HousingListing> listings = new ArrayList<>();
    static HousingListing listing1 = new HousingListing();
    static HousingListing listing2 = new HousingListing();
    static HousingListing listing3 = new HousingListing();
    static UserPreference preference1 = new UserPreference();
    static UserPreference preference2 = new UserPreference();

    @BeforeAll
    static void setup() {
        listing1.setRate(1000.0);
        listing1.setNumBeds(3);
        listing1.setNumBaths(1.5);
        listing1.setIsFurnished(true);

        listing2.setRate(2000.0);
        listing2.setNumBeds(4);
        listing2.setNumBaths(2.5);
        listing2.setIsFurnished(false);

        listing3.setRate(700.0);
        listing3.setNumBeds(0);
        listing3.setNumBaths(1.0);
        listing3.setIsFurnished(false);

        listings.add(listing1);
        listings.add(listing2);
        listings.add(listing3);

        preference1.setMaxRent(2000.0);
        preference1.setMinNumberOfBedRooms(3);
        preference1.setMinNumberOfBathrooms(2.0);
        preference1.setIsFurnished(false);

        preference2.setMaxRent(1000.0);
        preference2.setMinNumberOfBedRooms(1);
        preference2.setMinNumberOfBathrooms(1.0);
        preference2.setIsFurnished(true);
    }


    @Test
    void findByExactPreferences_Works() {
        //Act
        List<HousingListing> foundListings = filterer.findByExactPreferences(preference1, listings);

        //Assert
        assertEquals(1, foundListings.size());
        assertEquals(listing2, foundListings.getFirst());

    }

    @Test
    void findByNonStrictPreferences() {
    }

    @Test
    void findBySpecificPreference() {
    }
}