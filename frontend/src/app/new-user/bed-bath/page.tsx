'use client'

import { Loader } from "lucide-react"
import { PageTurner } from "../PageTurner"
import { useState } from "react"
import { NewUserObjects, useGlobalContext } from "@/components/GlobalContext"
import { ButtonGroupButton } from "@/app/home/InnerFilters"
import { RawCoordinateUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests"

interface InputProps {
    initialValue?: number,
    field: keyof RawCoordinateUserPreferenceDto,
    setNewUserInfo: React.Dispatch<React.SetStateAction<NewUserObjects>>
}

export const ValueButtons = ({initialValue, field, setNewUserInfo}: InputProps) => {

    const {newUserInfo} = useGlobalContext();

    const values = [0,1,2,3,4];
    const [selectedValue, setSelectedValue] = useState<number>(initialValue || 0);

    const handleSetValue = (value: number) => {
        setSelectedValue(value);
        setNewUserInfo(prev => ({
            userType:prev.userType,
            newUserPreferencesDto:{
                ...prev.newUserPreferencesDto,
                [field]: value
            }
        }))
        console.log(newUserInfo);
    }

    return (
        <span className="row flex">
        {values.map((val, index) => (
            <ButtonGroupButton number={val} key={index} setValue={handleSetValue} label={val.toString() + "+"} isSelected={selectedValue === val}/>
        ))}
        </span>
    )
}

    //checks if all fields are covered for
    export const isUserInfoFilled = (newUserInfo:NewUserObjects, isIntern:boolean) => {
        const preferences = newUserInfo.newUserPreferencesDto;
        // Check basic field existence (for all user types)
        const hasBasicFields =
        !!preferences.jobLocationAddress &&
        !!preferences.cityOfEmployment &&
        preferences.maxRadius !== undefined &&
        preferences.maxRent !== undefined &&
        preferences.bedrooms !== undefined &&
        preferences.bathrooms !== undefined
        //preferences.isFurnished !== undefined; TODO implement later!!

        // If user is not an intern, we only need to check basic fields
        if (!isIntern) {
            return hasBasicFields;
        }

        // For interns, also check date fields and their validity
        const hasDateFields =
        !!preferences.startDate &&
        !!preferences.endDate;

        // Only validate date range if both dates exist
        const isDateRangeValid = hasDateFields ? 
        new Date(preferences.startDate!) < new Date(preferences.endDate!) : false;

        return hasBasicFields && hasDateFields && isDateRangeValid;
    }

const BedBathForm = () => {

    const {newUserInfo, setNewUserInfo, isIntern} = useGlobalContext();
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const isUserIntern = () => {
        return newUserInfo.userType === 'INTERN'
    }

    const isUserFieldsFilled = () => {
        return isUserInfoFilled(newUserInfo, isIntern);
    }

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <form className="flex flex-col gap-5 bg-slate-100 rounded-xl min-h-96 shadow-xl py-3 px-4">
                    <article className="flex flex-col justify-center h-full p-4 gap-7">
                        <legend className="text-2xl font-semibold pb-2">How many Beds & Baths do you need?</legend>
                        <div>
                            <label className="text-lg shadow-md">Bedrooms</label>
                            <ValueButtons initialValue={newUserInfo.newUserPreferencesDto.bedrooms} field="bedrooms" setNewUserInfo={setNewUserInfo}/>
                        </div>
                        <div>
                            <label className="text-lg shadow-md">Bathrooms</label>
                            <ValueButtons initialValue={newUserInfo.newUserPreferencesDto.bathrooms} field="bathrooms" setNewUserInfo={setNewUserInfo}/>
                        </div>
                    </article>
                    <nav className="flex justify-between mt-auto pt-4 items-center">
                        <PageTurner href="/new-user/max/" direction="left"/>
                        {isUserIntern()?
                        <PageTurner href="/new-user/bed-bath/" direction="right"/> :
                        <button disabled={!isUserFieldsFilled()} type="submit" className="btn btn-outline hover:bg-slate-200 hover:text-black flex items-center">
                        Confirm
                            <Loader size={22} className={`ml-2 animate-pulse ${isLoading ? "" : "hidden"}`}/>
                        </button>
                    }
                </nav>
            </form>
        </main>
    )
}

export default BedBathForm;