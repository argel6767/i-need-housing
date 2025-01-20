import { apiClient, bearerHeader, failedCallMessage } from "./apiConfig";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { HouseListing } from "@/interfaces/entities";

const MODULE_MAPPING = "/listings"

/**
 * holds Housing listings api calls
 */

/**
 * getListings in a given radius from a point
 * @param requestBody 
 * @returns 
 */
export const getListingsInArea = async(requestBody: GetListingsInAreaRequest): Promise<Array<HouseListing>> => {
    try {
        const response =  await apiClient.post(MODULE_MAPPING+"/area", requestBody, bearerHeader);
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
 * deletes listing
 * @param id 
 * @returns 
 */
export const deleteListing = async(id: number): Promise<any> => {
    try {
        const response = await apiClient.delete(`${MODULE_MAPPING}/${id}`);
        if (response.status === 204) {
            return response.data;
        }
        console.log(response.data);
        return response.data;
    }
    catch(error) {
        const errorMessage = failedCallMessage(error);
        console.log(errorMessage);
        return errorMessage;
    }
}