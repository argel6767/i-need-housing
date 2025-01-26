

export const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export const isValidEmail = (email: string): boolean => {
    return EMAIL_REGEX.test(email);
}
