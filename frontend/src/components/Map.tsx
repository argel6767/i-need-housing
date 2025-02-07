"use client"
import { HouseListing } from "@/interfaces/entities";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";

interface MapProps {
    latitude: number,
    longitude: number
    listings: Array<HouseListing>
}
export const Map = ({latitude, longitude, listings}):MapProps => {
    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY
    const position = {lat:37.77, lng:-122.40}
    const containerStyle = {
        width: "100%",
        height: "100%",  // Set a proper height
    };
    return (
        <main className="h-full">
           <LoadScript googleMapsApiKey={KEY??""}>
            <GoogleMap mapContainerStyle={containerStyle} zoom={12} center={position}>
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