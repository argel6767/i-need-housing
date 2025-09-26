import {apiClient, failedCallMessage} from "@/api/apiConfig";
import {AxiosError} from "axios";

export const checkCookie = async (): Promise<string> => {
    try {
        const response = await apiClient.post("/auths/cookie-status")
        return response.data;
    }
    catch (error) {
        return "Cookie not valid"
    }
}

export const login = async (request: AuthenticateDto): Promise<string> => {
    try {
        const response = await apiClient.post("/admin/login", request);
        const user = JSON.stringify(response.data);
        sessionStorage.setItem("user", JSON.stringify(user));
        return "User logged in";
    }
    catch (error) {
        console.error(failedCallMessage(error));
        if (error instanceof AxiosError) {
            return error?.response?.data;
        }
        return "Request failed with error";
    }
}

export const logout = async() => {
    try {
        const response = await apiClient.post('/auths/logout');
        return response.data;
    }
    catch(error) {
        console.log(failedCallMessage(error))
        return "user could not be logged out."
    }
}
