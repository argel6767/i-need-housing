"use client"

import { HouseListing } from "@/interfaces/entities"
import Image from "next/image"
import { Loading } from "./Loading"
import { useEffect, useRef, useState } from "react"
import { useGlobalContext } from "./GlobalContext"
import { ArrowImageCarousel } from "./Carousel"



interface ListingModalProps {
    listing?:HouseListing
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
}

//TODO Add listing details!!!
/**
 * the modal that will render when a listing card is pressed, showing more info about the listing
 * @param param0 
 * @returns 
 */
export const ListingModal = ({listing, setIsModalUp}: ListingModalProps) => {
    return (
        <main>
                    <span className="absolute top-0 right-0">
                        <button onClick={(e) => {
                            e.stopPropagation();
                            setIsModalUp(false);}} className="btn btn-sm btn-circle btn-ghost">✕</button>
                    </span>
                    <ArrowImageCarousel images={listing?.imageUrls}/>
                    <h3 className="font-bold text-lg">Hello!</h3>
                    <p className="py-4">Press ESC key or click on ✕ button to close</p>
        </main>
    )
}

interface HousingCardProps {
    listing:HouseListing,
    setRenderedListing:React.Dispatch<React.SetStateAction<HouseListing | undefined>>,
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
}

export const HousingCard = ({listing, setRenderedListing, setIsModalUp}:HousingCardProps) => {

    const {setCenterLat = null, setCenterLong = null} = useGlobalContext();

    useEffect(() => {

    }, [listing])

    //changes the center point of the map to the coordinates of the listing, allowing for displaying of location on map
    const handleCenterPositionChange = () => {
        setCenterLat(listing.coordinates[0]);
        setCenterLong(listing.coordinates[1]);
        console.log(listing.coordinates)
    }

    const handlePropertySelection = () => {
        handleCenterPositionChange();
        setRenderedListing(listing);
        console.log(listing);
        setIsModalUp(true);
    }

    const hasImages = ():boolean => {
        return listing.imageUrls.length > 0;
    }


    return (
        <main className="hover:scale-105 hover:cursor-pointer transition-transform duration-300 rounded-lg bg-slate-200" onClick={handlePropertySelection}>
            <span className=" bg-base-200 shadow-xl">
                <img className="aspect-[300/175] w-full h-auto object-cover" src={hasImages()? listing.imageUrls[0] : "./placeholder.jpg"} alt="Property image"/> 
                <h2 className="text-lg text-center">{listing.title}</h2>
            </span>
        </main>
    )
}
