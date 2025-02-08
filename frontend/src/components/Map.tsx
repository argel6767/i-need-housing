"use client"
import { HouseListing } from "@/interfaces/entities";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";
import { useGlobalContext } from "./GlobalContext";
import { useEffect, useRef } from "react";

interface MapProps {
    latitude: number,
    longitude: number
    listings: Array<HouseListing>
}
export const Map = ({listings}:MapProps) => {

    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY
    const {centerLat = 0.0, centerLong = 0.0} = useGlobalContext();
    const position = {lat:0.0, lng:0.0}

    const containerStyle = {
        width: "100%",
        height: "100%",  // Set a proper height
    };
    const mapRef = useRef<google.maps.Map | null>(null);

     // Smoothly pan to the new center when centerLat or centerLong changes
    useEffect(() => {
    if (mapRef.current) {
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
            {listings.map((listing: HouseListing, index:number) => { // Add index as key
                        const markerPosition = {
                            lat: listing.coordinates[0],
                            lng: listing.coordinates[1],
                        };

                        return (
                            <Marker
                                key={index} // Important: Add a unique key!
                                position={markerPosition}
                            />
                        );
                    })}
            </GoogleMap>
        </LoadScript> 
        </main>
    )
}