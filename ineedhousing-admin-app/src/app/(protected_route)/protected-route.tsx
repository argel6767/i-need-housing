'use client';

import {useEffect, useState, ReactNode, useMemo, createContext, useContext} from 'react';
import { useRouter } from 'next/navigation';
import {checkCookie} from "@/api/authenication";


interface ProtectedContextType {
    isAuthorized:boolean,
    setIsAuthorized: React.Dispatch<React.SetStateAction<boolean>>,
    isAuthLoading:boolean,
    setIsAuthLoading: React.Dispatch<React.SetStateAction<boolean>>,
}

const ProtectedContext = createContext<ProtectedContextType | undefined>(undefined);

interface ProtectedRouteProps {
    children: ReactNode;
}

export const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const [isAuthorized, setIsAuthorized] = useState<boolean>(false);
    const [isAuthLoading, setIsAuthLoading] = useState<boolean>(true);
    const router = useRouter();

    const contextValues = useMemo(() => ({
        isAuthorized, setIsAuthorized, isAuthLoading, setIsAuthLoading
    }), [isAuthorized, isAuthLoading])


    useEffect(() => {
        // Check for token immediately on component mount
        const checkCookieStatus = async () => {
            const cookieStatus = await checkCookie();
            if (cookieStatus === "Token still valid") {
                setIsAuthorized(true);
                setIsAuthLoading(false);
            }
            else {
                setIsAuthorized(false);
                setIsAuthLoading(false);
                router.replace('/sign-in');
            }
        }

        checkCookieStatus()
    }, [router]);

    // Global 403 response interceptor
    useEffect(() => {
        // Create a global event listener for API responses
        const handleGlobal403 = (event: Event) => {
            const customEvent = event as CustomEvent;
            if (customEvent.detail?.status === 403) {
                console.log('403 detected - redirecting to sign in');
                setIsAuthorized(false);
                setIsAuthLoading(false);
                router.replace('/sign-in');
            }
        };

        // Listen for custom 403 events
        window.addEventListener('auth:unauthorized', handleGlobal403);

        return () => {
            window.removeEventListener('auth:unauthorized', handleGlobal403);
        };
    }, [router]);

    // display this when checking user's authentication status
    if (!isAuthorized || isAuthLoading) {
        return null;
    }

    // Only render children when authorized
    return <ProtectedContext.Provider value={contextValues}>{children}</ProtectedContext.Provider>;
}

export const useProtectedContext = (): ProtectedContextType => {
    const context = useContext(ProtectedContext);
    if (!context) {
        throw new Error("useProtectedContext must be used within ProtectedContext")
    }
    return context;
}