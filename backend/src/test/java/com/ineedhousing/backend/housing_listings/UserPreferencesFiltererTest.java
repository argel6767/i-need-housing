package com.ineedhousing.backend.housing_listings;

import com.ineedhousing.backend.user_search_preferences.UserPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferencesFiltererTest {

    private List<HousingListing> testListings;
    private UserPreference testPreference;

    @BeforeEach
    void setUp() {
        // Create test listings
        HousingListing listing1 = new HousingListing();
        listing1.setRate(1000.0);
        listing1.setNumBeds(2);
        listing1.setNumBaths(1.0);
        listing1.setIsFurnished(true);

        HousingListing listing2 = new HousingListing();
        listing2.setRate(2000.0);
        listing2.setNumBeds(3);
        listing2.setNumBaths(2.0);
        listing2.setIsFurnished(false);

        HousingListing listing3 = new HousingListing();
        listing3.setRate(3000.0);
        listing3.setNumBeds(1);
        listing3.setNumBaths(1.5);
        listing3.setIsFurnished(true);

        testListings = Arrays.asList(listing1, listing2, listing3);

        // Create test preference
        testPreference = new UserPreference();
        testPreference.setMaxRent(2000.0);
        testPreference.setMinNumberOfBedRooms(2);
        testPreference.setMinNumberOfBathrooms(1.0);
        testPreference.setIsFurnished(true);
        testPreference.setInternshipStart(LocalDate.now());
        testPreference.setInternshipEnd(LocalDate.now().plusMonths(3));
    }

    @Test
    void findByExactPreferences_ShouldReturnMatchingListings() {
        List<HousingListing> result = UserPreferencesFilterer.findByExactPreferences(testPreference, testListings);
        
        assertEquals(1, result.size());
        HousingListing matchingListing = result.get(0);
        assertTrue(matchingListing.getRate() <= testPreference.getMaxRent());
        assertTrue(matchingListing.getNumBeds() >= testPreference.getMinNumberOfBedRooms());
        assertTrue(matchingListing.getIsFurnished().equals(testPreference.getIsFurnished()));
    }

    @Test
    void findByExactPreferences_WithNoMatches_ShouldReturnEmptyList() {
        testPreference.setMaxRent(500.0); // Set an impossible condition
        List<HousingListing> result = UserPreferencesFilterer.findByExactPreferences(testPreference, testListings);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNonStrictPreferences_ShouldReturnPartialMatches() {
        List<HousingListing> result = UserPreferencesFilterer.findByNonStrictPreferences(testPreference, testListings);
        
        assertTrue(result.size() >= 1);
        // Should include listings that match at least one criterion
        assertTrue(result.stream().anyMatch(listing -> 
            listing.getRate() <= testPreference.getMaxRent() ||
            listing.getNumBeds() >= testPreference.getMinNumberOfBedRooms() ||
            listing.getNumBaths() >= testPreference.getMinNumberOfBathrooms() ||
            listing.getIsFurnished().equals(testPreference.getIsFurnished())
        ));
    }

    @Test
    void findByNonStrictPreferences_MatchesAnyPreference_ShouldReturnAllPartialMatches() {
        // Set strict conditions for all preferences except one
        testPreference.setMaxRent(-1.0);  // Impossible rent
        testPreference.setMinNumberOfBedRooms(100);  // Impossible number of bedrooms
        testPreference.setMinNumberOfBathrooms(100.0);  // Impossible number of bathrooms
        testPreference.setIsFurnished(true);  // But this one should match some listings
        
        List<HousingListing> result = UserPreferencesFilterer.findByNonStrictPreferences(testPreference, testListings);
        
        // Should return all furnished listings despite other impossible conditions
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(HousingListing::getIsFurnished));
        assertEquals(
            testListings.stream().filter(HousingListing::getIsFurnished).count(),
            result.stream().filter(HousingListing::getIsFurnished).count()
        );
    }

    @Test
    void findByNonStrictPreferences_WithEmptyListings_ShouldReturnEmptyList() {
        List<HousingListing> result = UserPreferencesFilterer.findByNonStrictPreferences(
            testPreference, 
            List.of()  // Empty list of listings
        );
        
        assertTrue(result.isEmpty());
    }

    @Test
    void findBySpecificPreference_Rate_ShouldReturnMatchingListings() {
        Map<String, Object> ratePreference = new HashMap<>();
        ratePreference.put("rate", 2000.0);
        
        List<HousingListing> result = UserPreferencesFilterer.findBySpecificPreference(ratePreference, testListings);
        
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(listing -> listing.getRate() <= 2000.0));
    }

    @Test
    void findBySpecificPreference_NumBeds_ShouldReturnMatchingListings() {
        Map<String, Object> bedsPreference = new HashMap<>();
        bedsPreference.put("numBeds", 2.0);
        
        List<HousingListing> result = UserPreferencesFilterer.findBySpecificPreference(bedsPreference, testListings);
        
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(listing -> listing.getNumBeds() >= 2));
    }

    @Test
    void findBySpecificPreference_IsFurnished_ShouldReturnMatchingListings() {
        Map<String, Object> furnishedPreference = new HashMap<>();
        furnishedPreference.put("isFurnished", true);
        
        List<HousingListing> result = UserPreferencesFilterer.findBySpecificPreference(furnishedPreference, testListings);
        
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(HousingListing::getIsFurnished));
    }

    @Test
    void findBySpecificPreference_InvalidPreference_ShouldThrowException() {
        Map<String, Object> invalidPreference = new HashMap<>();
        invalidPreference.put("invalidField", "value");
        
        assertThrows(IllegalArgumentException.class, () -> 
            UserPreferencesFilterer.findBySpecificPreference(invalidPreference, testListings)
        );
    }

    @Test
    void findBySpecificPreference_EmptyListings_ShouldReturnEmptyList() {
        Map<String, Object> preference = new HashMap<>();
        preference.put("rate", 2000.0);
        
        List<HousingListing> result = UserPreferencesFilterer.findBySpecificPreference(preference, List.of());
        
        assertTrue(result.isEmpty());
    }
}