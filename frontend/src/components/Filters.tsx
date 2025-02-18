"use client"
import { ReactNode, useEffect, useState } from "react"
import { ChevronDown } from 'lucide-react';
import { UserPreference } from "@/interfaces/entities";
import { useGlobalContext } from "./GlobalContext";
import { updateUserPreferences } from "@/endpoints/preferences";

interface RangeBarProps {
    initialRange: number
    setUpdatedPreferences: any
}

/**
 * component for choosing radius
 * @param param0 
 * @returns 
 */
const RangeBar = ({initialRange, setUpdatedPreferences}:RangeBarProps) => {
    const [range, setRange] = useState<number>(initialRange || 5);

    const handleRange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log(e.target.value);
        setRange(parseInt(e.target.value));
        setUpdatedPreferences((prev) => ({...prev, maxRadius: e.target.value}));
    };

    return (
        <div className="space-y-2">
            <div className="flex justify-between items-center">
                <label className="text-sm font-medium">Search Radius</label>
                <span className="text-sm font-medium w-16 text-right">{range} mi</span>
            </div>
            <input type="range" min={5} max={25} value={range} className="range bg-slate-400 w-full" step="5" onChange={handleRange}/>
            <div className="flex w-full justify-between text-xs text-primary px-1 ">
                <div className="flex flex-col items-center">
                    <span>|</span>
                    <span>05</span>
                </div>
                <div className="flex flex-col items-center">
                    <span>|</span>
                    <span>10</span>
                </div>
                <div className="flex flex-col items-center">
                    <span>|</span>
                    <span>15</span>
                </div>
                <div className="flex flex-col items-center">
                    <span>|</span>
                    <span>20</span>
                </div>
                <div className="flex flex-col items-center">
                    <span>|</span>
                    <span>25</span>
                </div>
            </div>
        </div>
    )
}

interface MaxPriceProps {
    maxPrice: number,
    setUpdatedPreferences: any
}

const MaxPrice = ({maxPrice, setUpdatedPreferences}: MaxPriceProps) => {
    const [price, setPrice] = useState<number>(maxPrice | 1000)
    return (
        <main>
            <label className="form-control w-full max-w-xs">
            <div className="label">
                <span className="label-text">Max Rent</span>
            </div>
            <input inputMode="numeric" maxLength={5} placeholder="Type here" pattern="\d*" className="input input-bordered w-full max-w-xs" value={price}/>
            </label>
        </main>
    )
}

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
        <div className="w-full border rounded-lg bg-white">
            <button onClick={onToggle}className="w-full p-4 flex justify-between items-center bg-slate-100 hover:bg-gray-50 transition-colors rounded-xl">
                <span className="font-medium text-gray-900">{label}</span>
                <ChevronDown className={`h-5 w-5 text-gray-500 transition-transform duration-200 ${isOpen ? 'transform rotate-180' : ''}`}/>
            </button>
        
            {isOpen && (
                <div className="absolute z-10 w-72 bg-white border rounded-b-lg shadow-lg">
                    <div className="p-4 border-t">
                        {children}
                    </div>
                </div>
            )}
        </div>
    )
}

interface FiltersProp {
    userPreferences:UserPreference | null
}

export const Filters = () => {
    const [openFilter, setOpenFilter] = useState<string | null>(null);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [isInitialized, setIsInitialized] = useState<boolean>(false);
    const [maxRadius, setMaxRadius] = useState<number>(5);
    const {userPreferences, setUserPreferences, listings} = useGlobalContext();
    const [updatedPreferences, setUpdatedPreferences] = useState<UserPreference>();

    //create a deep copy for an updated userPreference for any changes
    useEffect(() => {
        if (userPreferences && !isInitialized) {
            handleOriginalPreferences();
            setUpdatedPreferences(JSON.parse(JSON.stringify(userPreferences)));
            setMaxRadius(userPreferences.maxRadius);
            setIsInitialized(true);
        }
    }, [userPreferences]);

    //waits for any potential changes in preferences to prompt user to save
    useEffect(() => {
        if (isInitialized && updatedPreferences) {
            const hasChanged = JSON.stringify(updatedPreferences) !== JSON.stringify(userPreferences);
            setIsFiltersChanged(hasChanged);
            console.log('Preferences updated:', updatedPreferences);
        }
    }, [updatedPreferences, isInitialized, userPreferences]);

    const handleToggle = (filter: string) => {
        setOpenFilter(openFilter === filter ? null : filter);
    };

    const handleOriginalPreferences = () => {
        setMaxRadius(userPreferences.maxRadius);
    }
    
    //saves the updated preferences for user for later use
    const saveUserPreferences = async () => {
        setUserPreferences(updatedPreferences);
        const response = await updateUserPreferences(sessionStorage.getItem("email"), updateUserPreferences);
    }

    //TODO implement this once the endpoint in backend is fixed and api call is written
    const filterListingsByOriginalPreferences = async () => {
    }

    return (
        <main className="flex space-x-5 px-2 relative">
            <div className="relative">
                <CollapseDown label="Change Distance" isOpen={openFilter === 'distance'}onToggle={() => handleToggle('distance')}>
                    <RangeBar initialRange={maxRadius} setUpdatedPreferences={setUpdatedPreferences}/>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown label="Price"isOpen={openFilter === 'price'}onToggle={() => handleToggle('price')}>
                    <MaxPrice/>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown label="Other" isOpen={openFilter === 'other'} onToggle={() => handleToggle('other')}>
                    <div className="w-full">Other content here</div>
                </CollapseDown>
            </div>
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-20 border animate-fade ${!isInitialized && `hidden`}`}>Filter</button>
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-32 border animate-fade ${!isFiltersChanged && "hidden"}`} onClick={saveUserPreferences}>Save Changes</button>
        </main>
    )
}