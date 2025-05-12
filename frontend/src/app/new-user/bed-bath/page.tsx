'use client'

import { PageTurner } from "../PageTurner"
import { useState } from "react"
import { NewUserObjects, useGlobalContext } from "@/components/GlobalContext"
import { ButtonGroupButton } from "@/app/home/InnerFilters"
import { RawUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests"
import { SubmitUserInfoButton, UserInfoSubmissionForm } from "../UserPreferenceComponents"


interface InputProps {
    initialValue?: number,
    field: keyof RawUserPreferenceDto,
    setNewUserInfo: React.Dispatch<React.SetStateAction<NewUserObjects>>
}

const ValueButtons = ({initialValue, field, setNewUserInfo}: InputProps) => {

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
    }

    return (
        <span className="row flex">
        {values.map((val, index) => (
            <ButtonGroupButton number={val} key={index} setValue={handleSetValue} label={val.toString() + "+"} isSelected={selectedValue === val}/>
        ))}
        </span>
    )
}


const BedBathForm = () => {

    const {newUserInfo, setNewUserInfo, isIntern} = useGlobalContext();
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const isUserIntern = () => {
        return newUserInfo.userType === 'INTERN'
    }

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <UserInfoSubmissionForm setIsLoading={setIsLoading}>
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
                        <PageTurner href="/new-user/internship-length" direction="right"/> :
                        <SubmitUserInfoButton isLoading={isLoading}/>
                        }
                    </nav>
            </UserInfoSubmissionForm>
        </main>
    )
}

export default BedBathForm;