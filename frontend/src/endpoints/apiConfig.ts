import axios from 'axios';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL,
    // Add this line to ensure cookies are sent with cross-origin requests
    withCredentials: true
});/*
// Update your response interceptor for handling authentication errors
apiClient.interceptors.response.use(
    response => response,
    error => {
        // Handle 403 Forbidden (token expired)
        if (error?.response?.status === 403 || error?.response?.status === 401) {
            console.log("Authentication error, redirecting to login page");
            if (typeof window !== 'undefined') {
                // No need to remove token from sessionStorage
                window.location.href = '/sign-in'; // sign in page
            }
        }
        
        // CORS error handling remains the same
        if (!error.response || error.code === 'ERR_NETWORK' || error.message.includes('CORS')) {
            console.log("CORS or network error detected, redirecting to home page");
            if (typeof window !== 'undefined') {
                window.location.href = '/';
            }
        }
        
        return Promise.reject(error); // Make sure to return the error for further handling
    }
);
*/
export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
};