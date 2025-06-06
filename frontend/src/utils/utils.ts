import {QueryClient} from "@tanstack/react-query";


export const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export const isValidEmail = (email: string): boolean => {
    return EMAIL_REGEX.test(email);
}

const PASSWORD_REGEX = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*?])(?=\S+$).{8,}$/;

export const isValidPassword = (password: string): boolean => {
    return PASSWORD_REGEX.test(password);
}


export const DEFAULT_PROFILE_PICTURE_URL = "https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg";

export const GOOGLE_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY;

export const queryClient = new QueryClient({
    defaultOptions: {
        queries : {
            refetchOnWindowFocus: false,
            staleTime: 10 * 60 * 1000, //10 minutes
        }
    }
})

