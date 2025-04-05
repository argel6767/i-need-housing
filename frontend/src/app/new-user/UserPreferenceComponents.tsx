'use client'

import { useGlobalContext } from "@/components/GlobalContext"

export const Header = () => {

    return (
        <main className="flex-col justify-center items-center text-center size-full gap-3 p-10 space-y-2 border-b-4 shadow-lg">
            <h1 className="flex-1 text-3xl md:text-4xl lg:text-5xl">Welcome to INeedHousing!</h1>
            <h2 className="text-2xl md:text-3xl lg:text-4xl">Let's set up your preferences to personalize your housing options.</h2>
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
