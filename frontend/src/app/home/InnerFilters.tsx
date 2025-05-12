"use client"
import { UserPreference } from "@/interfaces/entities";
import { useState } from "react";

interface RangeBarProps {
    initialRange?: number
    setUpdatedPreferences?: any
}

/**
 * component for choosing radius
 * @param param
 * @returns
 */

export const RadiusBar = () => {
    return <div className="flex w-full justify-between text-xs text-primary px-1 ">
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
    </div>;
}
export const RangeBar = ({initialRange, setUpdatedPreferences}:RangeBarProps) => {
    const [range, setRange] = useState<number>(initialRange || 5);

    const handleRange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setRange(parseInt(e.target.value));
        setUpdatedPreferences((prev:any) => ({...prev, maxRadius: e.target.value}));
    };

    return (
        <div className="space-y-2">
            <div className="flex justify-between items-center">
                <label className="text-sm font-medium">Search Radius</label>
                <span className="text-sm font-medium w-16 text-right">{range} mi</span>
            </div>
            <input type="range" min={5} max={25} value={range} className="range bg-slate-400 w-full" step="5" onChange={handleRange}/>
            <RadiusBar/>
        </div>
    )
}

interface MaxPriceProps {
    maxPrice?: number,
    setUpdatedPreferences?: any
}

/**
 * Max price component
 * @param param
 * @returns 
 */
export const MaxPrice = ({maxPrice, setUpdatedPreferences}: MaxPriceProps) => {
    const [price, setPrice] = useState<number>(maxPrice || 0)

    const handlePriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPrice(Number(event.target.value.replace(/\D/g, "").slice(0,6)));
        setUpdatedPreferences((prev:any) => ({...prev, maxRent: event.target.value}));
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
    isSelected: boolean
}

 export const ButtonGroupButton = ({number, setValue, label, isSelected}: ButtonGroupButtonProps) => {

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
export const ValueButtons = ({setUpdatedPreferences, field, initialValue}: ValueButtonsProps) => {

    const values = [0,1,2,3,4];
    const [selectedValue, setSelectedValue] = useState<number>(initialValue);

    const handleSetValue = (value: number) => {
        setSelectedValue(value);
        setUpdatedPreferences((prev:any) => ({...prev, [field]: value}));
    }

    return (
        <span className="row flex">
        {values.map((val, index) => (
            <ButtonGroupButton number={val} key={index} setValue={handleSetValue}  label={val.toString() + "+"} isSelected={selectedValue === val}/>
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
        setUpdatedPreferences((prev:any) => ({...prev, [field]: event.target.value}));
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
export const OtherFilters = ({setUpdatedPreferences, updatedPreferences}: OtherFiltersProps) => {
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