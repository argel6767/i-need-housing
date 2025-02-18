"use client"
import { HouseListing } from "@/interfaces/entities";
import { GoogleMap, LoadScript, Marker, OverlayView } from "@react-google-maps/api";
import { useGlobalContext } from "./GlobalContext";
import { useEffect, useRef } from "react";

interface MapProps {
    latitude: number,
    longitude: number
    listings: Array<HouseListing>
    isLoading:boolean
}

/**
 * Map component utilizing @react-google-maps/api
 * @param param0 
 * @returns 
 */
export const Map = ({listings, isLoading}:MapProps) => {

    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY
    const {centerLat, centerLong} = useGlobalContext();
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
                        const markerPosition = {
                            lat: listing.coordinates[0],
                            lng: listing.coordinates[1],
                        };

                        return (
                            <div className="hover:cursor-pointer">
                                <Marker
                                key={listing.id}
                                position={markerPosition}
                                />
                            </div>
                        );
                    })}
            </GoogleMap>
        </LoadScript> 
        </main>
    )
}