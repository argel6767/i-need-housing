import axios from 'axios';
import { headers } from 'next/headers';
import { config } from 'process';

const BASE_URL = process.env.BACKEND_API_BASE_URL

export const apiClient = axios.create({
    baseURL: BASE_URL
})

export const bearerHeader = {
    headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem("token")
    }
}