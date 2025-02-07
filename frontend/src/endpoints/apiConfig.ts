import axios from 'axios';
import { config } from 'process';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL
});

export const bearerHeader = {
    headers: {
      'Authorization': typeof window !== "undefined" ? 'Bearer ' + sessionStorage.getItem("token") : ""
    }
};

export const getBearerHeader = () => {
    return {'Authorization': typeof window !== "undefined" ? 'Bearer ' + sessionStorage.getItem("token") : ""};
};

export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
};