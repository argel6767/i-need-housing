"use client"
import { HouseListing } from "@/interfaces/entities";
import { GoogleMap, LoadScript, Marker, OverlayView } from "@react-google-maps/api";
import { useGlobalContext } from "./GlobalContext";
import { useEffect, useRef } from "react";

interface MapProps {
    listings: Array<HouseListing>
    setRenderedListing: React.Dispatch<React.SetStateAction<HouseListing | undefined>>
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>
}

/**
 * Map component utilizing @react-google-maps/api
 * @param param 
 * @returns 
 */
export const Map = ({listings, setRenderedListing, setIsModalUp}:MapProps) => {

    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY
    const {centerLat, setCenterLat, centerLong, setCenterLong} = useGlobalContext();
    const position = {lat:centerLat, lng:centerLong}

    const containerStyle = {
        width: "100%",
        height: "100%",  // Set a proper height
    };
    const mapRef = useRef<google.maps.Map | null>(null);

     // Smoothly pan to the new center when centerLat or centerLong changes
     //uses global context for manipulation in HousingCard
    useEffect(() => {
    if (mapRef.current && centerLat && centerLong) {
        const newCenter = { lat: centerLat, lng: centerLong };
        mapRef.current.panTo(newCenter);
    }
    }, [centerLat, centerLong]);



    return (
        <main className="h-full">
           <LoadScript googleMapsApiKey={KEY??""}>
            <GoogleMap mapContainerStyle={containerStyle} zoom={12} center={position}  
            onLoad={(map) => {
          mapRef.current = map; // Initialize map reference
        }}>
            {listings.map((listing: HouseListing,) => {  //make marker for each listing
                        if (listing.coordinates) {
                           const markerPosition = {
                            lat: listing.coordinates[0],
                            lng: listing.coordinates[1],
                        };

                        //allows for clicking on marker to see housing
                        const handleListingClicked = () => {
                            setRenderedListing(listing);
                            setIsModalUp(true);
                            setCenterLat(listing.coordinates[0]);
                            setCenterLong(listing.coordinates[1]);
                        }

                        return (
                            <div className="hover:cursor-pointer">
                                <Marker onClick={handleListingClicked}
                                key={listing.id}
                                position={markerPosition}
                                />
                            </div>
                        ); 
                        }
                    })}
            </GoogleMap>
        </LoadScript> 
        </main>
    )
}