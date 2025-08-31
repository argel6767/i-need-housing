'use client'

import React, {createContext, useContext, useState, useMemo, ReactNode} from "react";
import {Loading} from "@/components/Loading";
import {HouseListing, UserPreference} from "@/interfaces/entities";
import {ListingsResultsPageDto} from "@/interfaces/responses/listingsResponses";
import {GetListingsInAreaRequestV2} from "@/interfaces/requests/housingListingRequests";

interface HomeContextType {
    filterRendered: ReactNode,
    setFilterRendered: React.Dispatch<React.SetStateAction<ReactNode>>,
    isFiltersChanged: boolean,
    setIsFiltersChanged: React.Dispatch<React.SetStateAction<boolean>>,
    isListingsFiltered: boolean,
    setIsListingsFiltered: React.Dispatch<React.SetStateAction<boolean>>,
    listings: HouseListing[],
    setListings: React.Dispatch<React.SetStateAction<HouseListing[]>>,
    listingsPage: ListingsResultsPageDto,
    setListingsPage: React.Dispatch<React.SetStateAction<ListingsResultsPageDto>>,
    getListingsRequest: GetListingsInAreaRequestV2 | null,
    setGetListingsRequest: React.Dispatch<React.SetStateAction<GetListingsInAreaRequestV2 | null>>
    initialPreferences: UserPreference | undefined,
    setInitialPreferences: React.Dispatch<React.SetStateAction<UserPreference| undefined>>
    isListingModalUp: boolean,
    setIsListingModalUp: React.Dispatch<React.SetStateAction<boolean>>,
    isFilterModalUp: boolean,
    setIsFilterModalUp: React.Dispatch<React.SetStateAction<boolean>>,
    isFiltering: boolean,
    setIsFiltering: React.Dispatch<React.SetStateAction<boolean>>,
    isSaving: boolean,
    setIsSaving: React.Dispatch<React.SetStateAction<boolean>>,
    isResetting: boolean,
    setIsResetting: React.Dispatch<React.SetStateAction<boolean>>
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
    const [listingsPage, setListingsPage] = useState<ListingsResultsPageDto>({housingListings: [], pageNumber: 1, totalPages: 1});
    const [getListingsRequest, setGetListingsRequest] = React.useState<GetListingsInAreaRequestV2 | null>(null);
    const [initialPreferences, setInitialPreferences] = useState<UserPreference>();
    const [isListingModalUp, setIsListingModalUp] = useState(false);
    const [isFilterModalUp, setIsFilterModalUp] = useState(false);
    const [isSaving, setIsSaving] = useState(false);
    const [isFiltering, setIsFiltering] = useState(false);
    const [isResetting, setIsResetting] = useState(false);

    const contextValues = useMemo(() => ({
        filterRendered, setFilterRendered, isFiltersChanged, setIsFiltersChanged, isListingsFiltered, setIsListingsFiltered, listings, setListings, listingsPage, setListingsPage, getListingsRequest, setGetListingsRequest, initialPreferences, setInitialPreferences,
        isListingModalUp, setIsListingModalUp, isFilterModalUp, setIsFilterModalUp, isFiltering, setIsFiltering, isSaving, setIsSaving, isResetting, setIsResetting
    }), [filterRendered, isFiltersChanged, isListingsFiltered, listings, listingsPage, setListingsPage, getListingsRequest, setGetListingsRequest, initialPreferences, isListingModalUp, isFilterModalUp, isFiltering, isSaving, isResetting]);

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