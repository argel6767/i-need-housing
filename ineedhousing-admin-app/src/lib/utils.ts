import {QueryClient} from "@tanstack/query-core";

export const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

export const queryClient = new QueryClient({
    defaultOptions: {
        queries : {
            refetchOnWindowFocus: false,
            staleTime: 10 * 60 * 1000, //10 minutes
        }
    }
})