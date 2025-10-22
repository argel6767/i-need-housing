'use client'
import {FavoriteListing, UserDto, UserPreference} from "@/interfaces/entities";
import { RawUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests";
import {createContext, useContext, useMemo, useState, ReactNode} from "react";

// Holds both the userType and newUserPreferencesDto to be saved until the user confirms their choices
export interface NewUserObjects {
    userType:string,
    newUserPreferencesDto: RawUserPreferenceDto
}

interface GlobalContextType {
    centerLat: number
    setCenterLat: React.Dispatch<React.SetStateAction<number>>
    centerLong: number
    setCenterLong: React.Dispatch<React.SetStateAction<number>>
    user: UserDto | null;
    setUser: React.Dispatch<React.SetStateAction<UserDto | null>>
    userPreferences: UserPreference | null;
    setUserPreferences: React.Dispatch<React.SetStateAction<UserPreference | null>>
    favoriteListings: FavoriteListing[]
    setFavoriteListings: React.Dispatch<React.SetStateAction<FavoriteListing[]>>
    newUserInfo: NewUserObjects,
    setNewUserInfo: React.Dispatch<React.SetStateAction<NewUserObjects>> //TODO move this to a NewUserContext component
    isIntern:boolean
    setIsIntern: React.Dispatch<React.SetStateAction<boolean>>
    isFirstTimeUser: boolean,
    setIsFirstTimeUser: React.Dispatch<React.SetStateAction<boolean>>,

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
    const [user, setUser] = useState<UserDto | null>(null);
    const [userPreferences, setUserPreferences] = useState<UserPreference | null>(null);
    const [favoriteListings, setFavoriteListings] = useState<FavoriteListing[]>([]);
    const [newUserInfo, setNewUserInfo] = useState<NewUserObjects>({userType: '',  newUserPreferencesDto:{}})
    const [isIntern, setIsIntern] = useState<boolean>(false);
    const [isFirstTimeUser, setIsFirstTimeUser] = useState<boolean>(false);

    const contextValue = useMemo(() => ({
        centerLat, setCenterLat, centerLong, setCenterLong, user, setUser, userPreferences, setUserPreferences, favoriteListings, setFavoriteListings,
        newUserInfo, setNewUserInfo, isIntern, setIsIntern, isFirstTimeUser, setIsFirstTimeUser,
    }), [centerLat, centerLong, user, userPreferences, favoriteListings, newUserInfo, isIntern, isFirstTimeUser]);

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