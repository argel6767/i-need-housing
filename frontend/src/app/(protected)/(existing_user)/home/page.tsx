"use client"

import {FilterModal, Filters} from "@/app/(protected)/(existing_user)/home/Filters";
import { Footer } from "@/components/Footer";
import { useGlobalContext } from "@/components/GlobalContext";
import { HousingSearch } from "@/components/HousingSearch";
import { ListingModal } from "@/components/HousingsList";

import { Map } from "@/components/Map";
import {LoggedInMobileNavbar, LoggedInNavBar} from "@/components/Navbar";
import { useGetListings, useGetUserPreferences, useGetFavoriteListings } from "@/hooks/hooks";
import {  HouseListing } from "@/interfaces/entities";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { useEffect, useState } from "react";
import {Modal} from "@/components/Modal";
import {useHomeContext} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {checkCookie} from "@/endpoints/auths";
import {useProtectedContext} from "@/app/(protected)/ProtectedRoute";
import {Loading} from "@/components/Loading";

const Home = () => {
    const {isAuthLoading} = useProtectedContext();
    const [requestBody, setRequestBody] = useState<GetListingsInAreaRequest | null>(null);
    const {listings, setListings, isListingModalUp, setIsListingModalUp, isFilterModalUp, setIsFilterModalUp} = useHomeContext();
    const {setFavoriteListings} = useGlobalContext();
    const [renderedListing, setRenderedListing] = useState<HouseListing | undefined>(undefined);
    const {setCenterLat, setCenterLong, setUserPreferences} = useGlobalContext();

    //fetch calls
    const {isLoading:isGrabbing, isError:isFetchingFailed, data:preferences} = useGetUserPreferences();
    const {isLoading, isError, data, refetch, isFetching} = useGetListings(requestBody, {enabled: !!requestBody});
    const {isLoading:isGettingFavorites, data:favorites, isError:isCallFailed} = useGetFavoriteListings();

    /** sets state of user preferences should it ever change via the query call */
    useEffect(() => {
        if (preferences) {
            setUserPreferences(preferences);
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

    if (isAuthLoading) {
        return (
            <div className={"flex justify-center items-center h-screen"}>
                <Loading loadingMessage={"Building Dashboard..."}/>
            </div>
        )
    }

    return (
            <main className="flex flex-col h-screen">
                <nav className={"block md:hidden"}>
                    <LoggedInMobileNavbar setIsModalUp={setIsFilterModalUp} refetch={refetch}/>
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
                    <HousingSearch  listings={listings} isLoading={isLoading} isFetching={isFetching} isGrabbingFavorites = {isGettingFavorites} setRenderedListing={setRenderedListing} setIsModalUp={setIsListingModalUp}/>
                </span>
                <footer className="w-full border-t-2">
                    <Footer/>
                </footer>
            </main>
    )
}

export default Home;