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
        <main className="hover:scale-105 transition-transform duration-300 rounded-lg bg-slate-200">
            <span className=" bg-base-200 shadow-xl">
                <img className="w-full" src={hasImages()? listing.imageUrls[0] : "https://picsum.photos/300"} alt="Property image"/> 
                <p>{listing.title} - Sourced from {listing.source}</p>
            </span>
        </main>
    )
}