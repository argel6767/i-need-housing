import { UserPreference } from "@/interfaces/entities";
import { apiClient, failedCallMessage } from "./apiConfig";
import { RawUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests";
import { AxiosError } from "axios";

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
export const createUserPreferences = async (email: string, requestBody: RawUserPreferenceDto): Promise<any> => {
    try {
        const response = await apiClient.post(`${MODULE_MAPPING}/coordinates/${email}`, requestBody);
        if (response.status === 201) {
            return response.data;
        }
        console.log(response.data);
        return null;
    }
    catch(error: any) {
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
export const updateUserPreferences = async (email: string, requestBody: UserPreference): Promise<any> => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/${email}`, requestBody);
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
        const response = await apiClient.put(`${MODULE_MAPPING}/`, requestBody);
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
    const email = sessionStorage.getItem("email");
    try {
        const response = await apiClient.get(`${MODULE_MAPPING}/${email}`);
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