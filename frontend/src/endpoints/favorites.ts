import { apiClient, failedCallMessage } from "./apiConfig";
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
        if (response.data === 200) {
            return response.data;
        }
        console.log(response.data);
        return [];
    }
    catch (error) {
        console.log(failedCallMessage(error));
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
        const response = await apiClient.put(`${MODULE_MAPPING}/${email}`, requestBody);
        if (response.status === 201) {
            return response.data;    
        }
        console.log(response.data);
        return [];
    }
    catch(error) {
        console.log(failedCallMessage(error));
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
        const response = await apiClient.post(`${MODULE_MAPPING}/${email}`, requestBody);
        if (response.status === 200) {
            return response.data;    
        }
        console.log(response.data);
        return [];
    }
    catch(error) {
        console.log(failedCallMessage(error));
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
        if (response.status === 200) {
            return response.data;  
        }
        console.log(response.data);
        return response.data;
    }
    catch(error) {
        console.log(error);
        return failedCallMessage(error);
    }
}