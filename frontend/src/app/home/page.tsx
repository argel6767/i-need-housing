"use client"

import { Filters } from "@/app/home/Filters";
import { Footer } from "@/components/Footer";
import { useGlobalContext } from "@/components/GlobalContext";
import { HousingSearch } from "@/components/HousingSearch";
import { Loading } from "@/components/Loading";
import { Map } from "@/components/Map";
import { LoggedInNavBar } from "@/components/Navbar";
import { filterListingsByPreferences } from "@/endpoints/listings";
import { useListings, useUserPreferences } from "@/hooks/hooks";
import { HouseListing, UserPreference } from "@/interfaces/entities";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { useEffect, useState } from "react";

const Home = () => {
    const [requestBody, setRequestBody] = useState<GetListingsInAreaRequest | null>(null);
    const [listings, setListings] = useState<HouseListing[]>([])
    const {setCenterLat, setCenterLong, setUserPreferences} = useGlobalContext();
    const [city, setCity] = useState<string>(""); 
    const {isLoading:isGrabbing, isError:isFetchingFailed, data:preferences} = useUserPreferences(sessionStorage.getItem("email"));
    const {isLoading, isError, data, refetch, isFetching} = useListings(requestBody, {enabled: !!requestBody});

    /** sets state of listings should it ever change via the query call */
    useEffect(() => {
        if (preferences) {
            console.log("Preferences received:", preferences);
            setUserPreferences(preferences);
            setCity(preferences.cityOfEmployment);
            const coords = preferences.cityOfEmploymentCoords;
            setCenterLat(coords[0]);
            setCenterLong(coords[1]);
            setRequestBody({latitude: coords[0], longitude: coords[1], radius:preferences.maxRadius});
        }
    }, [preferences]);
    
    //setting listings once they're fetched
    useEffect(() => {
        if (data) {
            setListings(data);
        }
    }, [data]);

     //TODO implement this once the endpoint in backend is fixed and api call is written
     const filterListings = async (id: number) => {
        setListings(await filterListingsByPreferences({listings: listings, id: id}));
    }

    return (
        <main className="flex flex-col h-screen">
            <nav >
                <LoggedInNavBar/>
            </nav>
            <div className="pt-2">
                <Filters filterListings={filterListings} refetch={refetch} setListings={setListings} />
            </div>
            <span className="flex relative flex-1 w-full rounded-lg py-2 overflow-x-hidden min-h-[45rem]">
                <div className="relative flex-grow min-w-0"><Map listings={listings}/></div>
                <HousingSearch city={city} listings={listings} isLoading={isLoading} isFetching={isFetching}/>
            </span>
            <div className="w-full border-t-2">
                <Footer/>
            </div>
        </main>
        
    )
}

export default Home;