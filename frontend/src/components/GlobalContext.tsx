'use client'
import { FavoriteListing, User, UserPreference } from "@/interfaces/entities";
import {createContext, useContext, useMemo, useState, ReactNode, use} from "react";

interface GlobalContextType {
    centerLat: number;
    setCenterLat: React.Dispatch<React.SetStateAction<number>>;
    centerLong: number;
    setCenterLong: React.Dispatch<React.SetStateAction<number>>;
    user: User | null;
    setUser: React.Dispatch<React.SetStateAction<User | null>>;
    userPreferences: UserPreference | null;
    setUserPreferences: React.Dispatch<React.SetStateAction<UserPreference | null>>;
    favoriteListings: FavoriteListing[]
    setFavoriteListings: React.Dispatch<React.SetStateAction<FavoriteListing[]>>
  }
  
  const GlobalContext = createContext<GlobalContextType | undefined>(undefined);
  

interface GlobalProviderProps {
    children?:ReactNode,
}

/**
 * Houses global state variables
 * @param param
 * @returns 
 */
export const GlobalProvider = ({children}:GlobalProviderProps) => {
    const [centerLat, setCenterLat] = useState<number>(0.0);
    const [centerLong, setCenterLong] = useState<number>(0.0);
    const [user, setUser] = useState<User | null>(null);
    const [userPreferences, setUserPreferences] = useState<UserPreference | null>(null);
    const [favoriteListings, setFavoriteListings] = useState<FavoriteListing[]>([]);

    const contextValue = useMemo(() => ({
        centerLat, setCenterLat, centerLong, setCenterLong, user, setUser, userPreferences, setUserPreferences, favoriteListings, setFavoriteListings
    }), [centerLat, centerLong, user, userPreferences, favoriteListings]);

    return (
        <GlobalContext.Provider value={contextValue}>
            {children}
        </GlobalContext.Provider>
    )
}

export const useGlobalContext = (): GlobalContextType => {
    const context = useContext(GlobalContext);
    
    if (context === undefined) {
        throw new Error('useGlobalContext must be used within a GlobalProvider');
    }
    return context;
};