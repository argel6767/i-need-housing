import axios from 'axios';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL,
    // Add this line to ensure cookies are sent with cross-origin requests
    withCredentials: true
});



export const failedCallMessage = (error: unknown): string => {
    return `Something went wrong and the api call failed: ${JSON.stringify(error)}`
};

