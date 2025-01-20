import { request } from "http";
import { apiClient } from "./apiConfig";
import { apiClientWithCredentials } from "./apiConfig";
import { AuthenticateUserDto, ChangePasswordDto, ForgotPasswordDto, ResendEmailDto, VerifyUserDto } from "@/interfaces/requests/authsRequests";
import { User } from "@/interfaces/entities";

/**
 *  Holds auths api calls
 */


/**
 * register user
 * @param requestBody 
 */
export const register = async (requestBody: AuthenticateUserDto): Promise<User> => {
    try {
        const response = await apiClient.post("auths/register", requestBody);
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw (error);
    }
}

/**
 * login user
 * @param requestBody 
 */
export const login = async (requestBody: AuthenticateUserDto): Promise<User> => {
    try {
        const response = await apiClient.post("/auths/login", requestBody);
        sessionStorage.setItem("token", response.data.token);
        return response.data;
    }
    catch(error) {
        console.log(error);
        throw (error);
    }
}

/**
 * verifies user via verification code
 * @param requestBody 
 */
export const verifyUser = async (requestBody: VerifyUserDto): Promise<string> => {
    try {
        const response = await apiClient.post("/auths/verify", requestBody);
        return response.data;
    }
    catch(error: any) {
        console.log(error);
        return error.toString();
    }
}

/**
 * resends verification code email
 * @param email 
 */
export const resendVerificationEmail = async (email: ResendEmailDto): Promise<string> => {
    try {
        const response = await apiClient.post("/auths/resend", email);
        return response.data;
    }
    catch(error:any) {
        console.log(error);
        return error.toString();
    }
}

/**
 * updates password with the new given password
 * @param requestBody 
 */
export const changePassword = async (requestBody: ChangePasswordDto): Promise<any> => {
    try {
        const response = await apiClient.put("/auths/password", requestBody);
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw(error);
    }
}

/**
 * sends email for verification code for a rest password request
 * @param email 
 * @returns 
 */
export const sendPasswordVerification = async (email: string): Promise<any> => {
    try {
        const response = await apiClient.post(`/auths/${email}`);
        return response.data;
    }
    catch(error) {
        console.log(error);
        throw(error);
    }
}

/**
 * resets password when a user forgets it
 * @param requestBody 
 */
export const resetPassword = async (requestBody: ForgotPasswordDto): Promise<any> => {
    try {
        const response = await apiClient.put("/auths/reset", requestBody);
        return response.data;
    }
    catch(error) {
        console.log(error);
        throw error;
    }
}
