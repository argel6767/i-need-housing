'use client';

import { useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { LoadingBars } from '@/components/Loading';
import {checkCookie} from "@/endpoints/auths";

interface ProtectedRouteProps {
  children: ReactNode;
}

export const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const [isAuthorized, setIsAuthorized] = useState<boolean>(false);
  const router = useRouter();

  useEffect(() => {
    // Check for token immediately on component mount
    const checkCookieStatus = async () => {
        const cookieStatus = await checkCookie();
        if (cookieStatus === "Token still valid") {
            setIsAuthorized(true);
        }
        else {
            setIsAuthorized(false);
            router.replace('/sign-in');
        }
    }

    checkCookieStatus()
  }, [router]);

  // display this when checking user's authentication status
  if (!isAuthorized) {
    return (
        <main className='flex flex-col justify-center items-center h-screen'>
            <h1 className='text-4xl text-center animate-pulse'>Checking authentication status. Thank you for your patience! 😁</h1>
            <div className='flex justify-center'>
                <LoadingBars/>
            </div>

        </main>
    );
  }

  // Only render children when authorized
  return <>{children}</>;
}