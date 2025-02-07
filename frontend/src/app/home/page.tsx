"use client"

import { Footer } from "@/components/Footer";
import { HousingSearch } from "@/components/HousingSearch";
import { Map } from "@/components/Map";
import { LoggedInNavBar } from "@/components/Navbar";
import { useListings } from "@/hooks/hooks";
import { HouseListing } from "@/interfaces/entities";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { useEffect, useState } from "react";

const Home = () => {
    const [requestBody, setRequestBody] = useState<GetListingsInAreaRequest>({
        latitude:37.0,
        longitude: -121.0,
        radius:2
    });
    const [listings, setListings] = useState<HouseListing[]>([])
    const [city, setCity] = useState<String>("San Francisco"); //TODO update this when actually fetching logic is implemented!!!
    const {isLoading, error, data} = useListings(requestBody);

    /** sets state of listings should it ever change via the query call */
    useEffect(() => {
        if (data) {
            setListings(data);
        }
    }, [data]);

    return (
        <main className="flex flex-col h-screen">
            <nav className="">
                <LoggedInNavBar/>
            </nav>
            <span className="flex relative flex-1 w-full rounded-lg py-2 overflow-x-hidden">
                <div className="relative flex-grow min-w-0"><Map listings={listings}/></div>
                <HousingSearch city={city} listings={listings}/>
            </span>
            <div className="w-full border-t-2">
                <Footer/>
            </div>
        </main>
        
    )
}

export default Home;