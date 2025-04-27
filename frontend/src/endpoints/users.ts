import { SetUserTypeDto } from "@/interfaces/requests/userRequests";
import { apiClient, failedCallMessage } from "./apiConfig";

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