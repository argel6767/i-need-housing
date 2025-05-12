'use client';

import { useEffect, useState, ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { LoadingBars } from './Loading';
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

  // Show nothing during authentication check
  if (!isAuthorized) {
    return (
        <main className='flex-col justify-center items-center space-y-4 pt-20 px-3'>
            <h1 className='text-4xl md:text-5xl lg:text-6xl flex-1 text-center animate-pulse'>Slow Down There!</h1>
            <h2 className='text-3xl md:text-4xl lg:text-5xl text-center animate-pulse'>We're not quite ready to show you this. Thank you for you patience! 😁</h2>
            <h2 className='text-3xl md:text-4xl lg:text-5xl text-center animate-pulse'>Redirecting you back to our landing page.</h2>
            <div className='flex justify-center'>
                <LoadingBars/>
            </div>

        </main>
    );
  }

  // Only render children when authorized
  return <>{children}</>;
}