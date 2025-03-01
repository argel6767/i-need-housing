import axios from 'axios';
import { config } from 'process';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL
});

// Add this interceptor to dynamically add the token to every request
apiClient.interceptors.request.use(config => {
    if (typeof window !== "undefined") {
        const token = sessionStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    }
    return config;
    });


export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
};