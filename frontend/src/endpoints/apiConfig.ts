import axios from 'axios';
import { config } from 'process';

const BASE_URL = process.env.BACKEND_API_BASE_URL

export const apiClient = axios.create({
    baseURL: BASE_URL
})

export const bearerHeader = {
    headers: {
      'Authorization': typeof window !== "undefined" ? 'Bearer ' + sessionStorage.getItem("token") : ""
    }
  };

export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
}