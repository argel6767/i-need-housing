import { UserPreference } from "@/interfaces/entities";
import { apiClient, bearerHeader, failedCallMessage } from "./apiConfig";
import { RawCoordinateUserPreferenceRequest } from "@/interfaces/requests/userPreferencesRequests";

const MODULE_MAPPING = "/preferences";

/**
 * Holds User preferences api calls
 */

/**
 * creates a new UserPreference entity
 * @param email 
 * @param requestBody 
 * @returns 
 */
export const createUserPreferences = async (email: string, requestBody: RawCoordinateUserPreferenceRequest): Promise<any> => {
    try {
        const response = await apiClient.post(`${MODULE_MAPPING}/coordinates/${email}`, requestBody, bearerHeader);
        if (response.status === 201) {
            return response.data;
        }
        console.log(response.data);
        return null;
    }
    catch(error) {
        console.log(failedCallMessage(error));
        return null;
    }
}

/**
 * updates a user's preferences
 * @param email
 * @param requestBody 
 * @returns 
 */
export const updateUserPreferences = async (email: string, requestBody: UserPreference): Promise<any> => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/${email}`, requestBody, bearerHeader);
        if (response.status === 200) {
            return response.data
        }
        console.log(response.data);
        return null;
    }
    catch(error) {
        console.log(failedCallMessage(error));
        return null;
    }
}

export const updateUserPreferencesViaFilters = async (requestBody: UserPreference): Promise<UserPreference> => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/`, requestBody, bearerHeader);
        return response.data;
    }
    catch (error) {
        console.log(failedCallMessage(error));
        return requestBody;
    }
}

/**
 * Gets a user's preferences
 * @param email 
 * @returns 
 */
export const getUserPreferences = async (email:string) => {
    try {
        const response = await apiClient.get(`${MODULE_MAPPING}/${email}`, bearerHeader);
        if (response.status === 200) {
            return response.data;
        }
        console.log(response.data);
        return null;
    }
    catch(error) {
        console.log(failedCallMessage(error));
        return null;
    }
}