"use client"

import {FilterModal, Filters} from "@/app/(protected)/home/Filters";
import { Footer } from "@/components/Footer";
import { useGlobalContext } from "@/components/GlobalContext";
import { HousingSearch } from "@/components/HousingSearch";
import { ListingModal } from "@/components/HousingsList";

import { Map } from "@/components/Map";
import {LoggedInMobileNavbar, LoggedInNavBar} from "@/components/Navbar";
import { useListings, useUserPreferences, useFavoriteListings } from "@/hooks/hooks";
import {  HouseListing } from "@/interfaces/entities";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { useEffect, useState } from "react";
import {Modal} from "@/components/Modal";
import {HomeProvider} from "@/app/(protected)/home/HomeContext";

const Home = () => {
    const [requestBody, setRequestBody] = useState<GetListingsInAreaRequest | null>(null);
    const [listings, setListings] = useState<HouseListing[]>([])
    const {setFavoriteListings} = useGlobalContext();
    const [renderedListing, setRenderedListing] = useState<HouseListing | undefined>(undefined);
    const [isListingModalUp, setIsListingModalUp] = useState<boolean>(false);
    const [isFilterModalUp, setIsFilterModalUp] = useState<boolean>(false);
    const {setCenterLat, setCenterLong, setUserPreferences} = useGlobalContext();
    const [city, setCity] = useState<string>(""); 


      const {isLoading:isGrabbing, isError:isFetchingFailed, data:preferences} = useUserPreferences();
      const {isLoading, isError, data, refetch, isFetching} = useListings(requestBody, {enabled: !!requestBody});
      const {isLoading:isGettingFavorites, data:favorites, isError:isCallFailed} = useFavoriteListings();

    /** sets state of user preferences should it ever change via the query call */
    useEffect(() => {
        if (preferences) {
            setUserPreferences(preferences);
            setCity(preferences.cityOfEmployment);
            const coords = preferences.cityOfEmploymentCoords;
            setCenterLat(coords[0]);
            setCenterLong(coords[1]);
            setRequestBody({latitude: coords[0], longitude: coords[1], radius:preferences.maxRadius});
        }
    }, [preferences, setCenterLat, setCenterLong, setUserPreferences]); //possibly get rid of additional dependencies
    
    //setting listings once they're fetched
    useEffect(() => {
        if (data) {
            setListings(data);
        }
    }, [data]);

    //sets favorite listings once they fetched
    useEffect(() => {
        if(favorites) {
            setFavoriteListings(favorites)
        }
    }, [favorites, setFavoriteListings])

    return (
        <HomeProvider>
            <main className="flex flex-col h-screen">
                <nav className={"block md:hidden"}>
                    <LoggedInMobileNavbar setIsModalUp={setIsFilterModalUp}/>
                </nav>
                <nav className={"hidden md:block"}>
                    <LoggedInNavBar/>
                </nav>
                <span className="pt-2 hidden md:block">
                    <Filters refetch={refetch} listings={listings} setListings={setListings} />
                </span>
                {isListingModalUp && ( /** This modal is rendered when a user clicks on a specific listing off the listings sidebar */
                    <Modal onClick={() => setIsListingModalUp(true)}>
                    <ListingModal listing={renderedListing} setIsModalUp={setIsListingModalUp}/>
                    </Modal>
                )}
                {isFilterModalUp && (
                    <Modal onClick={() => setIsFilterModalUp(true)}>
                        <FilterModal setIsModalUp={setIsFilterModalUp}/>
                    </Modal>
                )}
                <span className="flex relative flex-1 w-full rounded-lg py-2 overflow-x-hidden min-h-[45rem]">
                    <div className="relative flex-grow min-w-0"><Map listings={listings} setRenderedListing={setRenderedListing} setIsModalUp={setIsListingModalUp}/></div>
                    <HousingSearch city={city} listings={listings} isLoading={isLoading} isFetching={isFetching} isGrabbingFavorites = {isGettingFavorites} setRenderedListing={setRenderedListing} setIsModalUp={setIsListingModalUp}/>
                </span>
                <footer className="w-full border-t-2">
                    <Footer/>
                </footer>
            </main>
        </HomeProvider>
    )
}

export default Home;