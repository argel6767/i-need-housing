"use client"
import { ReactNode, useState } from "react"
import { ChevronDown } from 'lucide-react';
import { UserPreference } from "@/interfaces/entities";

interface RangeBarProps {
    initialRange: number
}

/**
 * component for choosing radius
 * @param param0 
 * @returns 
 */
const RangeBar = ({initialRange}:RangeBarProps) => {
    const [range, setRange] = useState<number>(initialRange || 5);

    const handleRange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log(e.target.value);
        setRange(parseInt(e.target.value));
    };

    return (
        <div className="space-y-2">
            <div className="flex justify-between items-center">
                <label className="text-sm font-medium">Search Radius</label>
                <span className="text-sm font-medium w-16 text-right">{range} mi</span>
            </div>
            <input 
                type="range" 
                min={5} 
                max={25} 
                value={range} 
                className="range bg-slate-400 w-full" 
                step="5" 
                onChange={handleRange}
            />
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
            <button
                onClick={onToggle}
                className="w-full p-4 flex justify-between items-center bg-slate-100 hover:bg-gray-50 transition-colors rounded-xl"
            >
                <span className="font-medium text-gray-900">{label}</span>
                <ChevronDown 
                    className={`h-5 w-5 text-gray-500 transition-transform duration-200 ${
                        isOpen ? 'transform rotate-180' : ''
                    }`}
                />
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
    userPreferences?:UserPreference
}

export const Filters = ({userPreferences}: FiltersProp) => {
    const [openFilter, setOpenFilter] = useState<string | null>(null);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [maxRadius, setMaxRadius] = useState<number>(5);

    const handleToggle = (filter: string) => {
        setOpenFilter(openFilter === filter ? null : filter);
    };

    const handleChangeSaving = () => {
        setIsFiltersChanged((prev) => !prev);
    }

    const handleOriginalPreferences = () => {
        setMaxRadius(userPreferences?.maxRadius);
        
    }

    return (
        <main className="flex space-x-5 px-2 relative">
            <div className="relative">
                <CollapseDown 
                    label="Change Distance" 
                    isOpen={openFilter === 'distance'}
                    onToggle={() => handleToggle('distance')}
                >
                    <RangeBar initialRange={maxRadius}/>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown 
                    label="Price"
                    isOpen={openFilter === 'price'}
                    onToggle={() => handleToggle('price')}
                >
                    <div className="w-full">Price content here</div>
                </CollapseDown>
            </div>
            <div className="relative">
                <CollapseDown 
                    label="Other"
                    isOpen={openFilter === 'other'}
                    onToggle={() => handleToggle('other')}
                >
                    <div className="w-full">Other content here</div>
                </CollapseDown>
            </div>
            <button className={`bg-primary rounded-lg w-52 text-white hover:bg-[#457F9F] animate-fade hidden`}>Filter By Your Preferences</button>
            <button className={`bg-primary rounded-lg w-20 text-white hover:bg-[#457F9F] animate-fade ${!isFiltersChanged && "hidden"}`} onClick={handleChangeSaving}>Save</button>
        </main>
    )
}