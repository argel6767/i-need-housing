"use client"

import { HouseListing } from "@/interfaces/entities"
import Image from "next/image"
import { Loading } from "./Loading"
import { useEffect, useRef, useState } from "react"
import { useGlobalContext } from "./GlobalContext"
import { ArrowImageCarousel } from "./Carousel"
import { CircleX, Star } from "lucide-react"
import { addNewFavoriteListings, deleteAllFavorites, deleteFavoriteListings } from "@/endpoints/favorites"
import { favoriteListingsRequest } from "@/interfaces/requests/favoriteListingsRequests"



interface ListingModalProps {
    listing?:HouseListing
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
    isFavorited:boolean
}

//
/**
 * the modal that will render when a listing card is pressed, showing more info about the listing
 * @param param
 * @returns
 */
export const ListingModal = ({listing, setIsModalUp, isFavorited}: ListingModalProps) => {

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
                <p>No Original Listing</p>
            )
        }
        return (
            <a className="hover:underline" href={listing.listingUrl}>View Original Listing</a>
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
        <div>
            <span className="flex justify-end gap-4 pb-2">
                <FavoriteListing listing={listing} isFavoritedListing={isFavorited}/> 
                <button onClick={(e) => {e.stopPropagation(); setIsModalUp(false);}} className=""><CircleX className="hover:opacity-50" size={40}/></button>
            </span>
            <ArrowImageCarousel images={listing?.imageUrls}/>
            <h3 className="font-bold text-2xl py-2">{listing?.title}</h3>
            <span className="flex justify-between text-xl border-2 border-b-black pb-2"><h2>{listing?.numBeds} Bed(s) | {getNumBaths()} Bathroom(s)</h2><h2>${listing?.rate}/Month</h2></span>
            <article className="text-lg">
                <p className="py-4">{getDescription()}</p>
                <p>Address: {listing?.address}</p>
            <span className="flex justify-between">{getOriginalListingUrl()}<p>Source: {listing?.source}</p></span>
            </article> 
        </div>
    )
}

interface FavoriteListingProps {
    listing?: HouseListing,
    isFavoritedListing:boolean,
}

const FavoriteListing = ({listing, isFavoritedListing}: FavoriteListingProps) => {

    const [isUserHovering, setIsUserHovering] = useState<boolean>(false);
    const [isFavorited, setIsFavorited] = useState<boolean>(isFavoritedListing)
    const email = sessionStorage.getItem("email");

    const favoriteListing = async () => {
        const requestBody:favoriteListingsRequest = {listings: [listing!]};
        const data = await addNewFavoriteListings(email!, requestBody)
        setIsFavorited(true);
        console.log(data);
    }

    const unFavoriteListing = async () => {
        const requestBody = {favoriteListingIds: [listing?.id!]};
        const data = await deleteFavoriteListings(email!, requestBody);
        setIsFavorited(false);
        console.log(data);
    }


    const props = {
        size: 40,
        color:'#ef4444',
        ...((isUserHovering || isFavorited)  && { fill: '#ef4444' }),
        onMouseEnter: () => {setIsUserHovering(true)},
        onMouseLeave: () => {setIsUserHovering(false)},
        onClick: () => {isFavorited ? unFavoriteListing() : favoriteListing()}
    }
    
    
    return (
        <Star {...props} className="hover:cursor-pointer hover:scale-110 transition-transform duration-300"/> 
    )

}


interface HousingCardProps {
    listing:HouseListing,
    setRenderedListing:React.Dispatch<React.SetStateAction<HouseListing | undefined>>,
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
    isFavorited:boolean,
    setIsFavorited:React.Dispatch<React.SetStateAction<boolean>>
}

export const HousingCard = ({listing, setRenderedListing, setIsModalUp, isFavorited, setIsFavorited}:HousingCardProps) => {

    const {setCenterLat, setCenterLong} = useGlobalContext();

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
        setIsFavorited(isFavorited);
        console.log(listing);
        setIsModalUp(true);
    }

    const hasImages = ():boolean => {
        return listing.imageUrls.length > 0;
    }


    return (
        <main className="hover:scale-105 hover:cursor-pointer transition-transform duration-300 rounded-lg bg-slate-200" onClick={handlePropertySelection}>
            <span className=" bg-base-200 shadow-xl">
                <Image className="aspect-[300/175] w-full h-auto object-cover" src={hasImages()? listing.imageUrls[0] : "./placeholder.jpg"} alt="Property image"/> 
                <h2 className="text-lg text-center">{listing.title}</h2>
            </span>
        </main>
    )
}
