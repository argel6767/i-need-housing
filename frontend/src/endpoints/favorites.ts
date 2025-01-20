import { apiClient, bearerHeader } from "./apiConfig";
import { FavoriteListing, HouseListing } from "@/interfaces/entities";
import { favoriteListingsRequest } from "@/interfaces/requests/favoriteListingsRequests";

const MODULE_MAPPING = "/favorites";

/**
 * holds favorite listing api calls
 */

/**
 * get all user's favorite listings
 * @param email 
 * @returns 
 */
export const getAllFavoriteListings = async (email: string): Promise<Array<FavoriteListing>> => {
    try {
        const response = await apiClient.get(`${MODULE_MAPPING}/${email}`)
        return response.data;
    }
    catch (error) {
        console.log(error);
        return [];
    }
}

/**
 * add list of listings to user favorites
 * @param email 
 * @param requestBody 
 * @returns 
 */
export const addNewFavoriteListings = async (email: string, requestBody: favoriteListingsRequest): Promise<Array<FavoriteListing>> => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/${email}`, requestBody, bearerHeader);
        return response.data;
    }
    catch(error) {
        console.log(error);
        return [];
    }
}

/**
 * deletes the listings given 
 * @param email 
 * @param requestBody 
 * @returns 
 */
export const deleteFavoriteListings = async (email: string, requestBody: favoriteListingsRequest): Promise<Array<FavoriteListing>> => {
    try {
        const response = await apiClient.post(`${MODULE_MAPPING}/${email}`, requestBody, bearerHeader);
        return response.data;
    }
    catch(error) {
        console.log(error);
        return [];
    }
}

/**
 * deletes favorites
 * @param email 
 * @returns 
 */
export const deleteAllFavorites = async (email: string): Promise<string> => {
    try {
        const response = await apiClient.delete(`${MODULE_MAPPING}/${email}`)
        return response.data;
    }
    catch(error) {
        console.log(error);
        return "Something went wrong: " + error;
    }
}