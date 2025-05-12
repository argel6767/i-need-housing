
import { apiClient, failedCallMessage } from "./apiConfig";
import { AuthenticateUserDto, ChangePasswordDto, ForgotPasswordDto, ResendEmailDto, VerifyUserDto } from "@/interfaces/requests/authsRequests";


const MODULE_MAPPING = "/auths"
/**
 *  Holds auths api calls
 */

/**
 * checks status of cookie, valid or expired
 */
export const checkCookie = async () => {
    try {
        const response = await apiClient.post(MODULE_MAPPING+"/cookie-status");
        return await response.data;
    }
    catch (error: any) {
        console.log(failedCallMessage(error));
        return "Cookie expired!"
    }
}

/**
 * register user
 * @param requestBody 
 */
export const register = async (requestBody: AuthenticateUserDto): Promise<any> => {
    try {
        const response = await apiClient.post(MODULE_MAPPING+"/register", requestBody);
        if (response.status === 201) {
            return "user created";
        }
        console.log(response.data);
        return null;
    }
    catch (error: any) {
        console.log(failedCallMessage(error));
        return "user could not be created";
    }
}

/**
 * login user
 * @param requestBody 
 */
export const login = async (requestBody: AuthenticateUserDto): Promise<any> => {
    try {
        const response = await apiClient.post(MODULE_MAPPING +"/login", requestBody);
        if (!response.data.userPreferences) {
            return "new user";
        }
        return "logged in"; 
    }
    catch(error: any) {
        console.log(failedCallMessage(error));
        if (error.response.status === 401) {
            return "user is not verified";
        }
        return "login failed";
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

/**
 * verifies user via verification code
 * @param requestBody 
 */
export const verifyUser = async (requestBody: VerifyUserDto): Promise<string> => {
    try {
        const response = await apiClient.post(MODULE_MAPPING+"/verify", requestBody);
        if (response.data.includes("Cannot invoke \"java.time.LocalDateTime.isBefore(java.time.chrono.ChronoLocalDateTime)\"")) {
            return "User has already been verified!";
        }
        return response.data;
    }
    catch(error: any) {
        console.log(error);
        if (error.status === 400) {
            return "User has already been verified!";
        }
        return failedCallMessage(error);    
    }
}

/**
 * resends verification code email
 * @param email 
 */
export const resendVerificationEmail = async (email: ResendEmailDto): Promise<any> => {
    try {
        const response = await apiClient.post(MODULE_MAPPING+"/resend", email);
        if (response.status === 200) {
         return response.data;   
        }
        console.log(response.data);
        return null;
    }
    catch(error:any) {
        console.log(failedCallMessage(error));
        return null;
    }
}

/**
 * updates password with the new given password
 * @param requestBody 
 */
export const changePassword = async (requestBody: ChangePasswordDto): Promise<any> => {
    try {
        const response = await apiClient.put(MODULE_MAPPING+"/password", requestBody);
        if (response.status === 200) {
            return response.data; 
        }
        console.log(response.data);
        return null;
    }
    catch (error) {
        console.log(error);
        return failedCallMessage(error);
    }
}

/**
 * sends email for verification code for a rest password request
 * @param email 
 * @returns 
 */
export const sendPasswordVerification = async (email: string): Promise<any> => {
    try {
        const response = await apiClient.post(`${MODULE_MAPPING}/${email}`);
        if (response.status === 200) {
            return response.data; 
        }
        console.log(response.data);
        return null;
    }
    catch(error) {
        console.log(error);
        return failedCallMessage(error);
    }
}

/**
 * resets password when a user forgets it
 * @param requestBody 
 */
export const resetPassword = async (requestBody: ForgotPasswordDto): Promise<any> => {
    try {
        const response = await apiClient.put(MODULE_MAPPING+"/reset", requestBody);
        if (response.status === 200) {
            return response.data;
        }
        console.log(response.data);
        return null
    }
    catch(error) {
        console.log(error);
        return failedCallMessage(error);
    }
}
