"use client"

import { HouseListing } from "@/interfaces/entities"
import { useEffect, useRef, useState } from "react"
import { useGlobalContext } from "./GlobalContext"
import { ArrowImageCarousel } from "./Carousel"
import { CircleX, Star } from "lucide-react"
import { addNewFavoriteListings, deleteAllFavorites, deleteFavoriteListings } from "@/endpoints/favorites"
import { favoriteListingsRequest } from "@/interfaces/requests/favoriteListingsRequests"



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

    const [isFavorited, setIsFavorited] = useState<boolean>(false);
    const {favoriteListings} = useGlobalContext();


    //Checks to see if rendered listing is a favorited one
    useEffect(() => {
            const isCurrentlyFavorited = favoriteListings ? 
            favoriteListings.some(fav => fav.housingListing.id === listing?.id) : false;
            setIsFavorited(isCurrentlyFavorited);
    }, [favoriteListings, listing])

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

    if (!favoriteListings) {
        return (<div>Loading</div>)
    }

    return (
        <div>
            <span className="flex justify-end gap-4 pb-2">
                <FavoriteListing listing={listing} isFavoritedListing={isFavorited} setIsFavoritedListing={setIsFavorited}/> 
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
    setIsFavoritedListing: React.Dispatch<React.SetStateAction<boolean>>
}

const FavoriteListing = ({listing, isFavoritedListing, setIsFavoritedListing}: FavoriteListingProps) => {

    const [isUserHovering, setIsUserHovering] = useState<boolean>(false);
    const {favoriteListings, setFavoriteListings} = useGlobalContext();


    //adds listing to favorites
    const favoriteListing = async () => {
        const requestBody:favoriteListingsRequest = {listings: [listing!]};
        const data = await addNewFavoriteListings(requestBody)
        setIsFavoritedListing(true);
        setFavoriteListings(data);
    }

    //removes listing in both global array and in backend
    const unFavoriteListing = async () => {
        setFavoriteListings((prev) => { //remove listing in global list incase backend fails
            return prev.filter(favorite => favorite.id !== favoriteListing?.id)
        })

        const favoriteListing = favoriteListings.find(favoriteListing => favoriteListing.housingListing.id === listing?.id);
        const favoriteListingId = favoriteListing?.id;
        const data = await deleteFavoriteListings(favoriteListingId!);
        console.log("data: " + data)
        setIsFavoritedListing(false);
        setFavoriteListings(data);
    }


    const props = {
        size: 40,
        color:'#ef4444',
        ...(( isFavoritedListing || isUserHovering)  && { fill: '#ef4444' }),
        onMouseEnter: () => {setIsUserHovering(true)},
        onMouseLeave: () => {setIsUserHovering(false)},
        onClick: () => {isFavoritedListing ? unFavoriteListing() : favoriteListing()}
    }
    
    
    return (
        <Star {...props} className="hover:cursor-pointer hover:scale-110 transition-transform duration-300"/> 
    )

}


interface HousingCardProps {
    listing:HouseListing,
    setRenderedListing:React.Dispatch<React.SetStateAction<HouseListing | undefined>>,
    setIsModalUp:React.Dispatch<React.SetStateAction<boolean>>
}

export const HousingCard = ({listing, setRenderedListing, setIsModalUp}:HousingCardProps) => {

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
