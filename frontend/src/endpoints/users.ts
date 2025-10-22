import { SetUserTypeDto } from "@/interfaces/requests/userRequests";
import { apiClient, failedCallMessage } from "./apiConfig";
import {UserDto} from "@/interfaces/entities";

const MODULE_MAPPING = "/users"

/**
 * updates the user type
 * @param requestBody 
 * @returns 
 */
export const updateUserType = async (requestBody: SetUserTypeDto) => {
    try {
        const response = await apiClient.put(`${MODULE_MAPPING}/type`, requestBody);
        return response.data;
    }
    catch (error) {
        console.log(failedCallMessage(error));
        return "call failed!";
    }
}

export const getUser = async (): Promise<UserDto> => {
    const response = await apiClient.get(`${MODULE_MAPPING}/me`);
    return response.data;
}

export const deleteUser = async () => {
    await apiClient.delete(`${MODULE_MAPPING}/me`);
}