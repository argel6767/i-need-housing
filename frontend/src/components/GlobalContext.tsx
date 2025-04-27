'use client'
import { FavoriteListing, User, UserPreference } from "@/interfaces/entities";
import { RawCoordinateUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests";
import {createContext, useContext, useMemo, useState, ReactNode, use} from "react";

// Holds both the userType and newUserPreferencesDto to be saved until the user confirms their choices
interface NewUserObjects {
    userType:string,
    newUserPreferencesDto: RawCoordinateUserPreferenceDto
}

interface GlobalContextType {
    centerLat: number
    setCenterLat: React.Dispatch<React.SetStateAction<number>>
    centerLong: number
    setCenterLong: React.Dispatch<React.SetStateAction<number>>
    user: User | null;
    setUser: React.Dispatch<React.SetStateAction<User | null>>
    userPreferences: UserPreference | null;
    setUserPreferences: React.Dispatch<React.SetStateAction<UserPreference | null>>
    favoriteListings: FavoriteListing[]
    setFavoriteListings: React.Dispatch<React.SetStateAction<FavoriteListing[]>>
    newUserInfo: NewUserObjects,
    setNewUserInfo: React.Dispatch<React.SetStateAction<NewUserObjects>>
    newUserPreferencesDto: RawCoordinateUserPreferenceDto
    setNewUserPreferencesDto: React.Dispatch<React.SetStateAction<RawCoordinateUserPreferenceDto>>
    isIntern:boolean
    setIsIntern: React.Dispatch<React.SetStateAction<boolean>>
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
    const [newUserInfo, setNewUserInfo] = useState<NewUserObjects>({userType: '',  newUserPreferencesDto:{}})
    const [newUserPreferencesDto, setNewUserPreferencesDto] = useState<RawCoordinateUserPreferenceDto>({});
    const [isIntern, setIsIntern] = useState<boolean>(false);

    const contextValue = useMemo(() => ({
        centerLat, setCenterLat, centerLong, setCenterLong, user, setUser, userPreferences, setUserPreferences, favoriteListings, setFavoriteListings,
        newUserInfo, setNewUserInfo, newUserPreferencesDto, setNewUserPreferencesDto, isIntern, setIsIntern
    }), [centerLat, centerLong, user, userPreferences, favoriteListings, newUserPreferencesDto, isIntern]);

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