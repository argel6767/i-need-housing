package com.ineedhousing.backend.housing_listings.utils;

import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user_search_preferences.UserPreference;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class UserPreferencesFilterer {


    /**
     * filters listings in area by the preferences exactly
     * @param preferences
     * @param listingsInArea
     * @return
     */
    public static List<HousingListing> findByExactPreferences(UserPreference preferences, List<HousingListing> listingsInArea) {
        List<HousingListing> finalListings = listingsInArea.stream().filter(listing -> listing.getRate() <= preferences.getMaxRent())
                .filter(listing -> listing.getNumBeds() >= preferences.getMinNumberOfBedrooms())
                .filter(listing -> listing.getNumBaths() >= preferences.getMinNumberOfBathrooms())
                .filter(listing -> listing.getIsFurnished().equals(preferences.getIsFurnished()))
                .toList(); //TODO ADD listing date for HousingListing to allow for filtering
        return finalListings;
    }

    /**
     * Filters each field individually and then combines them to find the List of objects that adhere to at least one of the preferences
     * @param preferences
     * @param listingsInArea
     * @return
     */
    public static List<HousingListing> findByNonStrictPreferences(UserPreference preferences, List<HousingListing> listingsInArea) {
        Set<HousingListing> filteredByRate = listingsInArea.stream().filter(listing -> listing.getRate() <= preferences.getMaxRent()).collect(Collectors.toSet());
        Set<HousingListing> filteredByNumBeds = listingsInArea.stream().filter(listing -> listing.getNumBeds() >= preferences.getMinNumberOfBedrooms()).collect(Collectors.toSet());
        Set<HousingListing> filteredByNumBaths = listingsInArea.stream().filter(listing -> listing.getNumBaths() >= preferences.getMinNumberOfBathrooms()).collect(Collectors.toSet());Set<HousingListing> filteredByIsFurnished = listingsInArea.stream().filter(listing -> listing.getIsFurnished().equals(preferences.getIsFurnished())).collect(Collectors.toSet());
        HashSet<HousingListing> combinedListings = new HashSet<>();
        combinedListings.addAll(filteredByRate);
        combinedListings.addAll(filteredByNumBeds);
        combinedListings.addAll(filteredByNumBaths);
        combinedListings.addAll(filteredByIsFurnished);
        return combinedListings.stream().toList();
    }

    /**
     * filters by preference, any number of them
     * @param preferences
     * @param listingsInArea
     * @return
     */
    public static List<HousingListing> findByMultiplePreferences(Map<String, Object> preferences, List<HousingListing> listingsInArea) {
       Set<String> keySet = preferences.keySet();
       for (String key: keySet) {
        listingsInArea = determinePreference(listingsInArea, key, preferences.get(key));
       }
       return listingsInArea;
    }

    /**
     * filters listings by a specific preference only
     * @param preference
     * @param listingsInArea
     * @return
     */
    public static List<HousingListing> findBySpecificPreference(Map<String, Object> preference, List<HousingListing> listingsInArea ) {
        String preferenceName = preference.keySet().stream().findFirst().get();
        Object preferenceValue = preference.get(preferenceName);
        return determinePreference(listingsInArea, preferenceName, preferenceValue);
    }

    /**
     * Helper function that determines the preference type and applies the correct filter returning List
     * @param listingsInArea
     * @param preferenceName
     * @param preferenceValue
     * @return
     */
    private static List<HousingListing> determinePreference(List<HousingListing> listingsInArea, String preferenceName,
            Object preferenceValue) {
        switch (preferenceName) {
            case "rate" -> {
                return listingsInArea.stream().filter(listing -> listing.getRate() <= (Double) preferenceValue).toList();
            }
            case "numBeds" -> {
                return listingsInArea.stream().filter(listing -> listing.getNumBeds() >= (Integer) preferenceValue).toList();
            }
            case "numBaths" -> {
                return listingsInArea.stream().filter(listing -> listing.getNumBaths() >= (Double) preferenceValue).toList();
            }
            case "isFurnished" -> {
                return  listingsInArea.stream().filter(listing -> listing.getIsFurnished().equals(preferenceValue)).toList();
            }
            default -> throw new IllegalArgumentException("Invalid preference name" + preferenceName);
        }
    }

}
