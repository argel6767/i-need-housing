import axios from 'axios';

const BASE_URL = process.env.BACKEND_API_BASE_URL

export const apiClientWithCredentials = axios.create({
    baseURL: BASE_URL, withCredentials: true
});

export const apiClient = axios.create({
    baseURL: BASE_URL
})