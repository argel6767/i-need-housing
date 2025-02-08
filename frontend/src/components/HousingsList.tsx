"use client"

import { HouseListing } from "@/interfaces/entities"
import Image from "next/image"
import { Loading } from "./Loading"
import { useEffect, useRef, useState } from "react"

interface HousingCardProps {
    listing:HouseListing
    isLoading:boolean
}

interface ListingModalProps {
    listing:HouseListing
    handleIsModalUp: () => void
}

const ListingModal = ({listing, handleIsModalUp}: ListingModalProps) => {
    return (
        <>
            {/* Backdrop */}
            <div 
                className="fixed inset-0 bg-black/50 z-40"
                onClick={handleIsModalUp}
            />
            
            {/* Centered Modal */}
            <div 
                className="fixed inset-0 flex items-center justify-center z-50"
                onClick={(e) => e.stopPropagation()}
            >
                <div 
                    className="modal-box bg-white w-11/12 max-w-4xl"
                    onClick={(e) => e.stopPropagation()}
                >
                    <button 
                        onClick={(e) => {
                            e.stopPropagation();
                            handleIsModalUp();
                        }} 
                        className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                    >
                        ✕
                    </button>
                    <h3 className="font-bold text-lg">Hello!</h3>
                    <p className="py-4">Press ESC key or click on ✕ button to close</p>
                </div>
            </div>
        </>
    )
}

export const HousingCard = ({listing, isLoading}:HousingCardProps) => {

    const [isModalUp, setIsModalUp] = useState<boolean>(false);

    useEffect(() => {

    }, [listing])

    const handleIsModalUp = () => {
        setIsModalUp((prev:boolean) => !prev);
    }

    const hasImages = ():boolean => {
        return listing.imageUrls.length > 0;
    }

    if (isLoading || !listing) {
        return <Loading/>
    }

    return (
        <main className="hover:scale-105 hover:cursor-pointer transition-transform duration-300 rounded-lg bg-slate-200" onClick={handleIsModalUp}>
            <span className=" bg-base-200 shadow-xl">
                <img className="aspect-[300/175] w-full h-auto object-cover" src={hasImages()? listing.imageUrls[0] : "https://picsum.photos/300/175"} alt="Property image"/> 
                <h2 className="text-lg text-center">{listing.title}</h2>
            </span>
            {isModalUp && (
              <article>
                {/*TODO FIX THIS <ListingModal listing={listing} handleIsModalUp={handleIsModalUp}/> */}
            </article>  
            )}
        </main>
    )
}