'use client'
import {HouseListing} from "@/interfaces/entities";
import { GoogleMap, Marker, Circle} from "@react-google-maps/api";
import {useGlobalContext} from "./GlobalContext";
import {useEffect, useRef, useState} from "react";
import {useGoogleMaps} from "./GoogleMapContext";
import {Loading} from "@/components/Loading";
import {useFindRadiusInMeters} from "@/hooks/hooks";

interface MapProps {
    listings: Array<HouseListing>
    setRenderedListing: React.Dispatch<React.SetStateAction<HouseListing | undefined>>
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>
}

export const Map = ({listings, setRenderedListing, setIsModalUp}: MapProps) => {
    const { isLoaded } = useGoogleMaps();
    const {centerLat, setCenterLat, centerLong, setCenterLong, userPreferences} = useGlobalContext();
    const position = {lat: centerLat, lng: centerLong};
    const radius = useFindRadiusInMeters();
    const [circleCreated, setCircleCreated] = useState(false);

    const containerStyle = {
        width: "100%",
        height: "100%",
    };

    //map refs
    const mapRef = useRef<google.maps.Map | null>(null);
    const circleRef = useRef<google.maps.Circle | null>(null);

    /* TODO UNCOMMENT THIS WHEN ITS FIGURED OUT
    const options = {
        strokeColor: '#bb4430',
        strokeOpacity: 0.8,
        strokeWeight: 6,
        clickable: false,
        draggable: false,
        editable: false,
        visible: true,
        radius: radius,
        zIndex: 1
    }

    useEffect(() => {
        if (circleRef.current) {
            circleRef.current.setRadius(radius)
            console.log("Updating circle radius to:", radius);
        }
    }, [radius]);

     */

    useEffect(() => {
        if (mapRef.current && centerLat && centerLong) {
            const newCenter = { lat: centerLat, lng: centerLong };
            mapRef.current.panTo(newCenter);
        }
    }, [centerLat, centerLong]);


    if (!isLoaded || !userPreferences) {
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
                {/*circleCreated && <Circle options={options} center={position} onLoad={(circle) => {
                    circleRef.current = circle;
                    setCircleCreated(true);
                    console.log("Circle created");
                }}/> TODO FIGURE THIS OUT*/}
            </GoogleMap>
        </main>
    );
}