'use client'
import { User, UserPreference } from "@/interfaces/entities";
import {createContext, useContext, useMemo, useState, ReactNode, use} from "react";

const GlobalContext = createContext();

interface GlobalProviderProps {
    children?:ReactNode,
}

/**
 * Houses global state variables
 * @param param0 
 * @returns 
 */
export const GlobalProvider = ({children}:GlobalProviderProps) => {
    const [centerLat, setCenterLat] = useState<number>(0.0);
    const [centerLong, setCenterLong] = useState<number>(0.0);
    const [user, setUser] = useState<User | null>(null);
    const [userPreferences, setUserPreferences] = useState<UserPreference | null>(null);

    const contextValue = useMemo(() => ({
        centerLat, setCenterLat, centerLong, setCenterLong, user, setUser, userPreferences, setUserPreferences
    }), [centerLat, centerLong, user, userPreferences]);

    return (
        <GlobalContext.Provider value={contextValue}>
            {children}
        </GlobalContext.Provider>
    )
}

export const useGlobalContext = () => useContext(GlobalContext);