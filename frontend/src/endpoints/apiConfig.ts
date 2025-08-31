import axios from 'axios';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL,
    // Add this line to ensure cookies are sent with cross-origin requests
    withCredentials: true
});

apiClient.interceptors.response.use(
    (response) => {
        // Return successful responses as-is
        return response;
    },
    (error) => {
        // Check if the error is a 403 (Forbidden)
        if (error.response?.status === 403) {
            // Dispatch a custom event that the auth context will listen for
            const unauthorizedEvent = new CustomEvent('auth:unauthorized', {
                detail: { status: 403, error }
            });
            window.dispatchEvent(unauthorizedEvent);
        }

        // Always reject the promise so the calling code can still handle the error
        return Promise.reject(error);
    }
);



export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
};