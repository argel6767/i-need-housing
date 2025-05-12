

export const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export const isValidEmail = (email: string): boolean => {
    return EMAIL_REGEX.test(email);
}

export const GOOGLE_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY;
