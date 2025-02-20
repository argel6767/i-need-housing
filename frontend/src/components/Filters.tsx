"use client"
import { ReactNode, useEffect, useState } from "react"
import { ChevronDown, Loader } from 'lucide-react';
import { UserPreference } from "@/interfaces/entities";
import { useGlobalContext } from "./GlobalContext";
import { updateUserPreferencesViaFilters } from "@/endpoints/preferences";

interface RangeBarProps {
    initialRange: number
    setUpdatedPreferences: any
}

/**
 * component for choosing radius
 * @param param
 * @returns
 */
const RangeBar = ({initialRange, setUpdatedPreferences}:RangeBarProps) => {
    const [range, setRange] = useState<number>(initialRange || 5);

    const handleRange = (e: React.ChangeEvent<HTMLInputElement>) => {
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

/**
 * Max price component
 * @param param
 * @returns 
 */
const MaxPrice = ({maxPrice, setUpdatedPreferences}: MaxPriceProps) => {
    const [price, setPrice] = useState<number>(maxPrice)

    const handlePriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPrice(Number(event.target.value.replace(/\D/g, "").slice(0,6)));
        setUpdatedPreferences((prev) => ({...prev, maxRent: event.target.value}));
    }

    return (
        <main>
            <label className="form-control w-full max-w-xs">
            <div className="label">
                <span className="label-text text-lg">Max Rent</span>
            </div>
            <input inputMode="numeric" maxLength={5} placeholder="Type here" pattern="\d*" className="input input-bordered w-full max-w-xs" value={price} onChange={handlePriceChange}/>
            </label>
        </main>
    )
}

interface ButtonGroupButtonProps {
    number:number
    label:string
    setValue: any
    field: keyof UserPreference
    isSelected: boolean
}

const ButtonGroupButton = ({number, setValue, field, label, isSelected}: ButtonGroupButtonProps) => {

    return (
        <button onClick={() => {setValue(number)}}
            className={`rounded-md rounded-l-none ${isSelected? "bg-slate-200" :"bg-slate-100"} py-2 px-4 border border-transparent text-center text-sm  transition-all shadow-md hover:shadow-lg focus:bg-slate-200 focus:shadow-none active:bg-slate-200 hover:bg-slate-200 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none`}
            type="button" >{label}</button>
    )
}

interface ValueButtonsProps {
    setUpdatedPreferences: any
    field: keyof UserPreference
    initialValue: number
}

//The value buttons for choosing the number of an item
const ValueButtons = ({setUpdatedPreferences, field, initialValue}: ValueButtonsProps) => {

    const values = [0,1,2,3,4];
    const [selectedValue, setSelectedValue] = useState<number>(initialValue);

    const handleSetValue = (value: number) => {
        setSelectedValue(value);
        setUpdatedPreferences((prev) => ({...prev, [field]: value}));
    }

    return (
        <span className="row flex">
        {values.map((val, index) => (
            <ButtonGroupButton number={val} key={index} setValue={handleSetValue} field={field} label={val.toString() + "+"} isSelected={selectedValue === val}/>
        ))}
        </span>
    )
}

interface DatePickerProps {
    setUpdatedPreferences: any,
    initialValue: string
    field: keyof UserPreference
}

/**
 * Date Picker component for selecting internship start and end dates
 * @param param
 * @returns
 */
const DatePicker = ({ setUpdatedPreferences, initialValue, field }: DatePickerProps) => {
    // Initialize state with the initial value
    const [date, setDate] = useState<string>(initialValue);

    const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setDate(event.target.value);
        setUpdatedPreferences((prev) => ({...prev, [field]: event.target.value}));
    };

    return (
        <input type="date" value={date} onChange={handleDateChange}/>
    );
};

interface OtherFiltersProps {
    setUpdatedPreferences: any
    updatedPreferences: UserPreference,
}

/**
 * Component for Other Collapse down that has all other filtering
 * @param param 
 * @returns 
 */
const OtherFilters = ({setUpdatedPreferences, updatedPreferences}: OtherFiltersProps) => {
    return (
        <main>
            <span className="flex flex-col gap-6">
                <div className="flex-1 flex justify-between items-center gap-4">
                    <label className="flex-1">Bedrooms</label>
                    <ValueButtons setUpdatedPreferences={setUpdatedPreferences} field="minNumberOfBedrooms" initialValue={updatedPreferences.minNumberOfBedrooms}/>
                </div>
                <div className="flex justify-between items-center">
                    <label>Bathrooms</label>
                    <ValueButtons setUpdatedPreferences={setUpdatedPreferences} field="minNumberOfBathrooms" initialValue={updatedPreferences.minNumberOfBathrooms}/>
                </div>
                <div className="flex justify-between items-center">
                    <label>Internship Start Date</label>
                    <DatePicker setUpdatedPreferences={setUpdatedPreferences} initialValue={updatedPreferences.internshipStart} field="internshipStart"/>
                </div>
                <div className="flex justify-between items-center">
                    <label>Internship End Date</label>
                    <DatePicker setUpdatedPreferences={setUpdatedPreferences} initialValue={updatedPreferences.internshipEnd} field="internshipEnd"/>
                </div>
            </span>
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

/**
 * All different available filters for listings
 * @returns 
 */
export const Filters = () => {
    const [openFilter, setOpenFilter] = useState<string | null>(null);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [isInitialized, setIsInitialized] = useState<boolean>(false);
    const {userPreferences, setUserPreferences, listings} = useGlobalContext();
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

    //TODO implement this once the endpoint in backend is fixed and api call is written
    const filterListings = async () => {
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
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-24 border animate-fade flex items-center justify-center gap-2 shadow-lg ${!isInitialized && `hidden`}`}>Filter
                <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
            <button className={`bg-slate-100 hover:bg-gray-50 rounded-lg w-32 border animate-fade ${!isFiltersChanged && "hidden"} flex items-center justify-center gap-2 shadow-lg`} onClick={saveUserPreferences}>Save Changes
            <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/>
            </button>
        </main>
    )
}