'use client';

import { useRouter } from "next/navigation";
import { useGlobalContext } from "@/components/GlobalContext";
import { useEffect, useState, ReactNode } from "react";
import { LoadingBars } from "@/components/Loading";

interface NewUserRouteProps {
    children: ReactNode;
}

export const NewUserRoute = ({ children }: NewUserRouteProps) => {
    const router = useRouter();
    const { isFirstTimeUser } = useGlobalContext();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (isFirstTimeUser === null || isFirstTimeUser === undefined) {
            return; // Still loading from context
        }


        setLoading(false);

        // If NOT a first time user, redirect to home
        if (isFirstTimeUser === false) {
            router.push('/home');
        }
    }, [isFirstTimeUser, router]);

    // Show loading while we're determining user status
    if (loading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <LoadingBars />
            </div>
        );
    }

    // If not a first-time user, show redirect message while navigation happens
    if (isFirstTimeUser === false) {
        return (
            <div className="flex justify-center items-center h-screen">
                <h1 className="text-xl">Redirecting to Home...</h1>
            </div>
        );
    }

    // Only render children for first-time users
    return <>{children}</>;
};