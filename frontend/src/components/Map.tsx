'use client'
import {HouseListing} from "@/interfaces/entities";
import { GoogleMap, Marker} from "@react-google-maps/api";
import {useGlobalContext} from "./GlobalContext";
import {useEffect, useRef} from "react";
import {useGoogleMaps} from "./GoogleMapContext";
import {Loading} from "@/components/Loading";

interface MapProps {
    listings: Array<HouseListing>
    setRenderedListing: React.Dispatch<React.SetStateAction<HouseListing | undefined>>
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>
}

export const Map = ({listings, setRenderedListing, setIsModalUp}: MapProps) => {
    const { isLoaded } = useGoogleMaps();
    const {centerLat, setCenterLat, centerLong, setCenterLong} = useGlobalContext();
    const position = {lat: centerLat, lng: centerLong};

    const containerStyle = {
        width: "100%",
        height: "100%",
    };
    const mapRef = useRef<google.maps.Map | null>(null);

    useEffect(() => {
        if (mapRef.current && centerLat && centerLong) {
            const newCenter = { lat: centerLat, lng: centerLong };
            mapRef.current.panTo(newCenter);
        }
    }, [centerLat, centerLong]);

    if (!isLoaded) {
        return <div className="h-full flex items-center justify-center"><Loading/></div>;
    }

    return (
        <main className="h-full">
            <GoogleMap 
                mapContainerStyle={containerStyle} 
                zoom={12} 
                center={position}  
                onLoad={(map) => {
                    mapRef.current = map;
                }}
            >
                {listings.map((listing: HouseListing) => {
                    if (listing.coordinates) {
                        const markerPosition = {
                            lat: listing.coordinates[0],
                            lng: listing.coordinates[1],
                        };

                        const handleListingClicked = () => {
                            setRenderedListing(listing);
                            setIsModalUp(true);
                            setCenterLat(listing.coordinates[0]);
                            setCenterLong(listing.coordinates[1]);
                        }

                        return (
                            <div className="hover:cursor-pointer motion-preset-fade" key={listing.id}>
                                <Marker 
                                    onClick={handleListingClicked}
                                    position={markerPosition}
                                />
                            </div>
                        ); 
                    }
                    return null;
                })}
            </GoogleMap>
        </main>
    );
}