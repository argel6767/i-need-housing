'use client'

import React, {createContext, useContext, useState, useMemo} from "react";
import {DEFAULT_PROFILE_PICTURE_URL} from "@/utils/utils";

interface ExistingUserContextType {
    profilePictureUrl: string;
    setProfilePictureUrl: React.Dispatch<React.SetStateAction<string>>;
}

const ExistingUserContext = createContext<ExistingUserContextType | undefined>(undefined)

interface ExistingProviderProps {
    children: React.ReactNode;
}

/**
 * Houses Filter component state variables
 */

export const ExistingUserProvider = ({ children }: ExistingProviderProps) => {
    const [profilePictureUrl, setProfilePictureUrl] = useState<string>(DEFAULT_PROFILE_PICTURE_URL);

    const contextValues = useMemo(() => ({
        profilePictureUrl, setProfilePictureUrl
    }), [profilePictureUrl]);

    return (
        <ExistingUserContext.Provider value={contextValues}>
            {children}
        </ExistingUserContext.Provider>
    )
}

export const useExistingUserContext = (): ExistingUserContextType => {
    const context = useContext(ExistingUserContext);
    if (!context) {
        throw new Error('useHomeContext must be used within HomeContext');
    }
    return context;
}