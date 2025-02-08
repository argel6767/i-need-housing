"use client"

import { HouseListing } from "@/interfaces/entities"
import Image from "next/image"
import { Loading } from "./Loading"
import { useEffect } from "react"

interface HousingCardProps {
    listing:HouseListing
    isLoading:boolean
}


export const HousingCard = ({listing, isLoading}:HousingCardProps) => {

    useEffect(() => {

    }, [listing])

    const hasImages = ():boolean => {
        return listing.imageUrls.length > 0;
    }

    if (isLoading || !listing) {
        return <Loading/>
    }

    return (
        <main className="hover:scale-105 hover:cursor-pointer transition-transform duration-300 rounded-lg bg-slate-200">
            <span className=" bg-base-200 shadow-xl">
                <img className="aspect-[300/175] w-full h-auto object-cover" src={hasImages()? listing.imageUrls[0] : "https://picsum.photos/300/175"} alt="Property image"/> 
                <h2 className="text-lg text-center">{listing.title}</h2>

            </span>
        </main>
    )
}