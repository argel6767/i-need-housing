"use client"

import { HouseListing } from "@/interfaces/entities"
import Image from "next/image"
import { Loading } from "./Loading"
import { useEffect, useRef, useState } from "react"
import { useGlobalContext } from "./GlobalContext"
import { ArrowImageCarousel } from "./Carousel"
import { CircleX } from "lucide-react"



interface ListingModalProps {
    listing?:HouseListing
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
}



//
/**
 * the modal that will render when a listing card is pressed, showing more info about the listing
 * @param param
 * @returns
 */
export const ListingModal = ({listing, setIsModalUp}: ListingModalProps) => {

    //gets description or returns default text when there is none
    const getDescription = () => {
        if (!listing?.description) {
            return "No Description Available."
        }
        return listing.description
    }

    //gets listing url or returns default text when there is none.
    const getOriginalListingUrl = () => {
        if (!listing?.listingUrl) {
            return (
                "N/A"
            )
        }
        return (
            <a className="hover:underline" href={listing.listingUrl}>Here</a>
        );
    }

    //gets number of bathrooms or default text
    const getNumBaths = () => {
        if (!listing?.numBaths) {
            return "?";
        }
        return listing?.numBaths;
    }

    return (
        <main>
                    <span className="absolute top-0 right-0">
                        <button onClick={(e) => {
                            e.stopPropagation();
                            setIsModalUp(false);}} className="btn btn-sm btn-circle btn-ghost"><CircleX className="hover:opacity-50" width={40} height={40}/></button>
                    </span>
                    <ArrowImageCarousel images={listing?.imageUrls}/>
                    <h3 className="font-bold text-2xl py-1">{listing?.title}</h3>
                    <span className="flex justify-between text-xl border-2 border-b-black pb-2"><h2>{listing?.numBeds} Bed(s) | {getNumBaths()} Bathroom(s)</h2><h2>${listing?.rate}/Month</h2></span>
                    <article className="text-lg">
                        <p className="py-4">{getDescription()}</p>
                        <p>Address: {listing?.address}</p>
                    <span className="flex justify-between"><p className="flex gap-2">Original Listing: {getOriginalListingUrl()}</p> <p>Source: {listing?.source}</p></span>
                    </article>
                    
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
