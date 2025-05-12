import { apiClient, failedCallMessage} from "./apiConfig";
import { ExactPreferencesDTO, GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { HouseListing, UserPreference } from "@/interfaces/entities";

const MODULE_MAPPING = "/listings"

/**
 * holds Housing listings api calls
 */

/**
 * getListings in a given radius from a point
 * @param requestBody 
 * @returns 
 */
export const getListingsInArea = async(requestBody: GetListingsInAreaRequest | null): Promise<Array<HouseListing>> => {
    if (!requestBody) {
        console.warn("Null request body. Halting call!");
        return [];
    }
    try {
        const response =  await apiClient.get(`${MODULE_MAPPING}/area`, {
            params: {
                latitude: requestBody.latitude,
                longitude: requestBody.longitude,
                radius: requestBody.radius
            }
        });
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
 * get Listing info
 * @param id 
 * @returns 
 */
export const getListingInfo = async(id: number): Promise<any> => {
    try {
        const response = await apiClient.get(`${MODULE_MAPPING}/${id}`);
        if (response.status === 200) {
            return response.data;
        }
        console.log(response.data);
        return null;
    }
    catch (error) {
        console.log(failedCallMessage(error));
        return null;
    }
}

/**
 * filters listings by their preferences
 * @param request 
 * @returns 
 */
export const filterListingsByPreferences = async (request: ExactPreferencesDTO): Promise<HouseListing[]> => {
    try {
        const response = await apiClient.post(`${MODULE_MAPPING}/filter/exact`, request);
        if (response.status === 204) {
            return [];
        }
        return response.data;
    }
    catch (error) {
        console.log(failedCallMessage(error));
        return request.listings
    }
}