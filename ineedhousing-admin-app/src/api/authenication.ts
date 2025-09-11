import {apiClient, failedCallMessage} from "@/api/apiConfig";

export const checkCookie = async (): Promise<string> => {
    try {
        const response = await apiClient.post("auths/cookie-status")
        return response.data;
    }
    catch (error) {
        return "Cookie not valid"
    }
}

export const login = async (request: AuthenticateDto): Promise<UserDto | null> => {
    try {
        const response = await apiClient.post("admin/login", request);
        return response.data;
    }
    catch (error) {
        console.error(failedCallMessage(error));
        return null;
    }
}