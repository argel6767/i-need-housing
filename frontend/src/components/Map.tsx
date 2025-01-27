"use client"
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";
export const Map = () => {
    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY
    const position = {lat:53.54, lng:10}
    const containerStyle = {
        width: "50%",
        height: "100%",  // Set a proper height
    };
    return (
        <main className="h-full">
           <LoadScript googleMapsApiKey={KEY??""}>
            <GoogleMap mapContainerStyle={containerStyle} zoom={9} center={position}>
            </GoogleMap>
        </LoadScript> 
        </main>
    )
}