import axios from 'axios';

const BASE_URL = process.env.BACKEND_API_BASE_URL

export const apiClient = axios.create({
    baseURL: BASE_URL
})