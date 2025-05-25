'use client'

import { RadiusBar } from "@/app/(protected)/(existing_user)/home/InnerFilters";
import { NewUserObjects, useGlobalContext } from "@/components/GlobalContext";
import { useState } from "react";
import { PageTurner } from "../PageTurner";

interface InputProps {
    maxRent?:number,
    maxRadius?:number
    setNewUserInfo: React.Dispatch<React.SetStateAction<NewUserObjects>>
}
const RangeBar = ({setNewUserInfo, maxRadius}:InputProps) => {
    const [range, setRange] = useState<number>(maxRadius || 5);

    const handleRange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setRange(parseInt(e.target.value));
        setNewUserInfo(prev => ({
            userType:prev.userType,
            newUserPreferencesDto: {
                ...prev.newUserPreferencesDto,
                maxRadius:range
            }
        }))
    };

    return (
        <div className="space-y-2">
            <div className="flex justify-between items-center">
                <label className="text-lg font-medium">Search Radius</label>
                <span className="text-sm font-medium w-16 text-right">{range} mi</span>
            </div>
            <input type="range" min={5} max={25} value={range} className="range bg-slate-400 w-full" step="5" onChange={handleRange}/>
            <RadiusBar/>
        </div>
    )
}

const MaxPrice = ({setNewUserInfo, maxRent}:InputProps) => {
    const [price, setPrice] = useState<number>(maxRent || 1000)

    const handlePriceChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const rentValue = Number(event.target.value.replace(/\D/g, "").slice(0,6));
        setPrice(rentValue);
        setNewUserInfo(prev => ({
            userType:prev.userType,
            newUserPreferencesDto: {
                ...prev.newUserPreferencesDto,
                maxRent:rentValue
            }
        }))
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

const MaxForm = () => {
    const {setNewUserInfo, newUserInfo} = useGlobalContext();

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <div className="flex flex-col gap-5 bg-slate-100 rounded-xl min-h-96 shadow-xl py-3">
            <article className="flex flex-col justify-center h-full p-4 gap-5">
            <legend className="text-2xl font-semibold pb-2">What is your max rent & radius?</legend>
                <MaxPrice setNewUserInfo={setNewUserInfo} maxRent={newUserInfo.newUserPreferencesDto.maxRent}/>
                <RangeBar setNewUserInfo={setNewUserInfo} maxRadius={newUserInfo.newUserPreferencesDto.maxRadius}/>
            </article>
            <nav className="flex justify-between mt-auto pt-4">
                <PageTurner href="/new-user/location/" direction="left"/>
                <PageTurner href="/new-user/bed-bath/" direction="right"/>
            </nav>
            </div>
        </main>
    )
}

export default MaxForm;