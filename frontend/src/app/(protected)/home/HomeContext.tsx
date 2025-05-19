'use client'

import React, {createContext, useContext, useState, useMemo, ReactNode} from "react";
import {Loading} from "@/components/Loading";
import {HouseListing, UserPreference} from "@/interfaces/entities";

interface HomeContextType {
    filterRendered: ReactNode,
    setFilterRendered: React.Dispatch<React.SetStateAction<ReactNode>>,
    isFiltersChanged: boolean,
    setIsFiltersChanged: React.Dispatch<React.SetStateAction<boolean>>,
    isListingsFiltered: boolean,
    setIsListingsFiltered: React.Dispatch<React.SetStateAction<boolean>>,
    listings: HouseListing[],
    setListings: React.Dispatch<React.SetStateAction<HouseListing[]>>,
    initialPreferences: UserPreference | undefined,
    setInitialPreferences: React.Dispatch<React.SetStateAction<UserPreference| undefined>>
    isListingModalUp: boolean,
    setIsListingModalUp: React.Dispatch<React.SetStateAction<boolean>>,
    isFilterModalUp: boolean,
    setIsFilterModalUp: React.Dispatch<React.SetStateAction<boolean>>,
}

const HomeContext = createContext<HomeContextType | undefined>(undefined)

interface HomeProviderProps {
    children: React.ReactNode;
}

/**
 * Houses Filter component state variables
 */

export const HomeProvider = ({ children }: HomeProviderProps) => {
    const [filterRendered, setFilterRendered] = useState<ReactNode>(<div><Loading loadingMessage={"Rendering Filter..."}/></div>);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [isListingsFiltered, setIsListingsFiltered] = useState<boolean>(false);
    const [listings, setListings] = useState<HouseListing[]>([]);
    const [initialPreferences, setInitialPreferences] = useState<UserPreference>();
    const [isListingModalUp, setIsListingModalUp] = useState(false);
    const [isFilterModalUp, setIsFilterModalUp] = useState(false);

    const contextValues = useMemo(() => ({
        filterRendered, setFilterRendered, isFiltersChanged, setIsFiltersChanged, isListingsFiltered, setIsListingsFiltered, listings, setListings, initialPreferences, setInitialPreferences,
        isListingModalUp, setIsListingModalUp, isFilterModalUp, setIsFilterModalUp
    }), [filterRendered, isFiltersChanged, isListingsFiltered, listings, initialPreferences, isListingModalUp, isFilterModalUp]);

    return (
        <HomeContext.Provider value={contextValues}>
            {children}
        </HomeContext.Provider>
    )
}

export const useHomeContext = (): HomeContextType => {
    const context = useContext(HomeContext);
    if (!context) {
        throw new Error('useHomeContext must be used within HomeContext');
    }
    return context;
}