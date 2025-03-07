"use client"
import { ReactNode, useEffect, useState } from "react"
import { ChevronDown, Loader } from 'lucide-react';
import { UserPreference } from "@/interfaces/entities";
import { useGlobalContext } from "../../components/GlobalContext";
import { updateUserPreferencesViaFilters } from "@/endpoints/preferences";
import { RangeBar, MaxPrice, OtherFilters } from "@/app/home/InnerFilters";

interface CollapseDownProps {
    children: ReactNode;
    label:string;
    isOpen: boolean;
    onToggle: () => void;
}

/**
 * Collapse down component for filters
 * @param param
 * @returns 
 */
const CollapseDown = ({children, label, isOpen, onToggle}: CollapseDownProps) => {
    return (
        <div className="w-full border rounded-lg bg-white shadow-lg">
            <button onClick={onToggle}className="w-full p-4 flex justify-between items-center bg-slate-100 hover:bg-gray-50 transition-colors rounded-xl">
                <span className="font-medium text-gray-900">{label}</span>
                <ChevronDown className={`h-5 w-5 text-gray-500 transition-transform duration-200 ${isOpen ? 'transform rotate-180' : ''}`}/>
            </button>
        
            {isOpen && (
                <div className="absolute z-10 min-w-72 w-auto bg-white border rounded-b-lg shadow-lg">
                    <div className="p-4 border-t">
                        {children}
                    </div>
                </div>
            )}
        </div>
    )
}

interface FiltersProps {
    filterListings: (id: number) => Promise<void>,
    refetch: any
    setListings: any
}

/**
 * All different available filters for listings
 * @returns 
 */
export const Filters = ({filterListings, refetch, setListings}: FiltersProps) => {
    const [openFilter, setOpenFilter] = useState<string | null>(null);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [isInitialized, setIsInitialized] = useState<boolean>(false);
    const [isListingsFiltered, setIsListingsFiltered] = useState<boolean>(false);
    const {userPreferences, setUserPreferences} = useGlobalContext();
    const [updatedPreferences, setUpdatedPreferences] = useState<UserPreference>();
    const [isLoading, setIsLoading] = useState<boolean>(false);

    //create a deep copy for an updated userPreference for any changes
    useEffect(() => {
        if (userPreferences && !isInitialized) {
            setUpdatedPreferences(JSON.parse(JSON.stringify(userPreferences)));
            setIsInitialized(true);
        }
    }, [userPreferences]);

    //waits for any potential changes in preferences to prompt user to save
    useEffect(() => {
        if (isInitialized && updatedPreferences) {
            const hasChanged = JSON.stringify(updatedPreferences) !== JSON.stringify(userPreferences);
            setIsFiltersChanged(hasChanged);
        }
    }, [updatedPreferences, isInitialized, userPreferences]);

    //checks if the CollapseDown component selected is the one currently open, if not closes the currently opened first
    const handleToggle = (filter: string) => {
        setOpenFilter(openFilter === filter ? null : filter);
    };

    //saves the updated preferences for user for later use
    const saveUserPreferences = async () => {
        setIsLoading(true);
        const response = await updateUserPreferencesViaFilters(updatedPreferences!);
        setUserPreferences(response);
        setUpdatedPreferences(response);
        setIsLoading(false);
    }

    //calls the filtering endpoint from the parent component using the id of the user's preferences
    const handleFiltering = async () => {
        setIsLoading(true);
        await filterListings(userPreferences.id);
        setIsLoading(false);
        setIsListingsFiltered(true);
    }

    //refetches the listings to reset from filtered state
    const handleRefetch = async () => {
        setIsLoading(true);
        const response = await refetch();
        setListings(response.data);
        setIsListingsFiltered(false);
        setIsLoading(false);
    }

    //skeleton until updatedPreferences is set
    if (!updatedPreferences) {
        return (
            <main className="flex space-x-5 px-2 relative">
            <div className="relative animate-pulse bg-slate-100">
            </div>
            <div className="relative animate-pulse bg-slate-100">
            </div>
            <div className="relative animate-pulse bg-slate-100">
            </div>
            <button disabled className="animate-pulse bg-slate-100"></button>
            </main>
        )
    }

    return (
        <main className="flex space-x-5 px-2 relative animate-fade">
            <div className="relative">
                <CollapseDown label="Change Distance" isOpen={openFilter === 'distance'}onToggle={() => handleToggle('distance')}>
                    <RangeBar initialRange={updatedPreferences.maxRadius} setUpdatedPreferences={setUpdatedPreferences}/>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown label="Price"isOpen={openFilter === 'price'}onToggle={() => handleToggle('price')}>
                    <MaxPrice maxPrice={updatedPreferences.maxRent} setUpdatedPreferences={setUpdatedPreferences}/>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown label="Other" isOpen={openFilter === 'other'} onToggle={() => handleToggle('other')}>
                    <OtherFilters setUpdatedPreferences={setUpdatedPreferences} updatedPreferences={updatedPreferences}/>
                </CollapseDown>
            </div>
            {/** //TODO Add more filtering options later
             * <div className="relative">
                <CollapseDown label="Filter" isOpen={openFilter === 'filter'} onToggle={() => handleToggle('filter')}>hello</CollapseDown>
            </div>
             */}
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-24 border animate-fade flex items-center justify-center gap-2 shadow-lg ${!isInitialized && `hidden`}`} onClick={handleFiltering}>Filter
                <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-32 border animate-fade ${!isFiltersChanged && "hidden"} flex items-center justify-center gap-2 shadow-lg`} onClick={saveUserPreferences}>Save Changes
            <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/>
            </button>
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-36 border animate-fade ${!isListingsFiltered && "hidden"} flex items-center justify-center gap-2 shadow-lg`} onClick={handleRefetch}>Reset Listings
            <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/>
            </button>
        </main>
    )
}