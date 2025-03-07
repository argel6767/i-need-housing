import axios from 'axios';

export const apiClient = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL
});

// interceptor to dynamically add the token to every request
apiClient.interceptors.request.use(config => {
    if (typeof window !== "undefined") {
        const token = sessionStorage.getItem("token");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    }
    return config;
    });

// interceptor to check for any 403 ie the user's token has been expired
apiClient.interceptors.response.use(response => response, //ie leave successful call alone
    error => {
        if (error.response && error.response.status === 403) { //forbidden status code
            console.log("Token expired redirecting back landing page");
            if (typeof window != 'undefined') {
                sessionStorage.removeItem('token');
                window.location.href = '/'; //landing page
            }
        } 
    }
);


export const failedCallMessage = (error: any): string => {
    return `Something went wrong and the api call failed: ${error}`
};