'use client'

import { NewUserObjects, useGlobalContext } from "@/components/GlobalContext"
import { createUserPreferences } from "@/endpoints/preferences"
import { updateUserType } from "@/endpoints/users"
import { SetUserTypeDto } from "@/interfaces/requests/userRequests"
import { Loader } from "lucide-react"
import { ReactNode, useState } from "react"
import { sleep } from "../utils/utils"
import { useRouter } from 'next/navigation';

export const PreferencesHeader = () => {

    return (
        <main className="flex-col justify-center items-center text-center size-full gap-3 p-10 space-y-2 border-b-4 shadow-lg">
            <h1 className="flex-1 text-3xl md:text-4xl lg:text-5xl">Welcome to INeedHousing!</h1>
            <h2 className="text-2xl md:text-3xl lg:text-4xl">Let's personalize your housing options.</h2>
        </main>
        
    )
}

export const BreadCrumbs = () => {
    const {isIntern} = useGlobalContext();
    
    return (
        <div className="breadcrumbs text-xs sm:text-md md:text-lg lg:text-xl text-center flex justify-center">
            <ul>
                <li>User Type</li>
                <li>Location</li>
                <li>Pricing</li>
                <li>Bed & Bath</li>
                {isIntern && <li>Internship Dates</li>}
            </ul>
        </div>
    )
}

interface SubmitUserInfoButtonProps {
    isLoading:boolean
}

export const SubmitUserInfoButton = ({isLoading}:SubmitUserInfoButtonProps) => {
    const {newUserInfo, isIntern} = useGlobalContext();

    //checks if all fields are filled before allow user to submit
    const isUserInfoFilled = () => {
        const preferences = newUserInfo.newUserPreferencesDto;
        // Check basic field existence (for all user types)
        const hasBasicFields =
        !!preferences.jobLocationAddress &&
        preferences.cityOfEmploymentAddress !== undefined &&
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
        preferences.startDate !== undefined &&
        !!preferences.endDate !== undefined;
        // Only validate date range if both dates exist
        const isDateRangeValid = hasDateFields ? 
        new Date(preferences.startDate!) < new Date(preferences.endDate!) : false;
        return hasBasicFields && hasDateFields && isDateRangeValid;
    }

    return (
        <button type="submit" className="btn btn-outline hover:bg-slate-200 hover:text-black flex items-center">
        Confirm<Loader size={22} className={`ml-2 animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
    )
}

export const submitUserInfo = async(userInfo: NewUserObjects) => {
    const email = sessionStorage.getItem('email')!;
    const requestBody: SetUserTypeDto = {
        email: email,
        userType: userInfo.userType
    }
    const requests =  [updateUserType(requestBody), createUserPreferences(email, userInfo.newUserPreferencesDto)]

    const [userTypeResponse, newUserPreferenceResponse] = await Promise.all(requests);

    return [userTypeResponse, newUserPreferenceResponse]
}

interface UserInfoSubmissionFormProps {
    children: ReactNode
    setIsLoading:React.Dispatch<React.SetStateAction<boolean>>
}

export const UserInfoSubmissionForm = ({children, setIsLoading}: UserInfoSubmissionFormProps) => {
    const router = useRouter();
    const [errorMessage, setErrorMessage] = useState<string>('Error try again');
    const [isError, setIsError] = useState<boolean>(false);
    const {newUserInfo} = useGlobalContext();
    
    //sends new user info to backend while handling the loading/error state
    const saveUserInfo = async (e: React.MouseEvent | React.FormEvent) => {
        e.preventDefault(); // Add this line
        setIsLoading(true);
        const responses = await submitUserInfo(newUserInfo);
        if (responses[0] === 'call failed!') {
            setErrorMessage('Failed to set User Type, try again.')
            setIsLoading(false);
            setIsError(true)
            await sleep(1500);
            setIsError(false);
        }
        else if (responses[0] === '400 request') {
            setErrorMessage('One or more preferences were not correctly filled, make sure to fill all before submitting')
            setIsLoading(false);
            setIsError(true)
            await sleep(1500);
            setIsError(false);
        }
        else {
            setIsLoading(false);
            router.push("/home")
        }
    }
    

    return (
        <form onSubmit={saveUserInfo} className="flex flex-col gap-5 bg-slate-100 rounded-xl min-h-96 shadow-xl py-3 px-4">
            {children}
            {isError && 
            <p className="text-red-400 text-center">{errorMessage}</p>}
        </form>
    )
}