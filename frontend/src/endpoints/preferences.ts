import { UserPreference } from "@/interfaces/entities";
import { apiClient, failedCallMessage } from "./apiConfig";
import { RawUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests";


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
export const createUserPreferences = async ( requestBody: RawUserPreferenceDto): Promise<any> => {
    try {
        console.log(requestBody)
        requestBody.isFurnished=false;
        const response = await apiClient.post(`${MODULE_MAPPING}/addresses`, requestBody);
        if (response.status === 201) {
            return response.data;
        }
        console.log(response.data);
        return null;
    }
    catch(error: any) {
        console.log(error.body)
        console.log(failedCallMessage(error));
        if (error.status === 400) {
            return 'One or more preferences were not correctly filled, make sure to fill all before submitting'
        }
        return "Something went wrong! Try again.";
    }
}

/**
 * updates a user's preferences
 * @param email
 * @param requestBody
 * @returns
 */
export const updateUserPreferences = async (requestBody: UserPreference): Promise<any> => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/}`, requestBody);
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
        console.log(requestBody)
        const response = await apiClient.put(`${MODULE_MAPPING}/filters`, requestBody);
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
export const getUserPreferences = async () => {
    try {
        const response = await apiClient.get(`${MODULE_MAPPING}/me`);
        if (response.status === 200) {
            console.log(response.data);
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