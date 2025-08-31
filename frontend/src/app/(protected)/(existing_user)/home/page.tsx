"use client"

import {FilterModal, Filters} from "@/app/(protected)/(existing_user)/home/Filters";
import { Footer } from "@/components/Footer";
import { useGlobalContext } from "@/components/GlobalContext";
import { HousingSearch } from "@/components/HousingSearch";
import { ListingModal } from "@/components/HousingsList";

import { Map } from "@/components/Map";
import {LoggedInMobileNavbar, LoggedInNavBar} from "@/components/Navbar";
import {useGetUserPreferences, useGetFavoriteListings, useGetListingsV2} from "@/hooks/hooks";
import {  HouseListing } from "@/interfaces/entities";
import { useEffect, useState } from "react";
import {Modal} from "@/components/Modal";
import {useHomeContext} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {useProtectedContext} from "@/app/(protected)/ProtectedRoute";
import {Loading} from "@/components/Loading";

const Home = () => {
    const {isAuthLoading} = useProtectedContext();
    const {listingsPage,setListingsPage, getListingsRequest, setGetListingsRequest, isListingModalUp, setIsListingModalUp, isFilterModalUp, setIsFilterModalUp} = useHomeContext();
    const {setFavoriteListings} = useGlobalContext();
    const [renderedListing, setRenderedListing] = useState<HouseListing | undefined>(undefined);
    const {setCenterLat, setCenterLong, setUserPreferences} = useGlobalContext();

    //fetch calls
    const {isLoading:isGrabbing, isError:isFetchingFailed, data:preferences} = useGetUserPreferences();
    const {isLoading, isError, data, refetch, isFetching} = useGetListingsV2(getListingsRequest, {enabled: !!getListingsRequest});
    const {isLoading:isGettingFavorites, data:favorites, isError:isCallFailed} = useGetFavoriteListings();

    /** sets state of user preferences should it ever change via the query call */
    useEffect(() => {
        if (preferences) {
            setUserPreferences(preferences);
            const coords = preferences.cityOfEmploymentCoords;
            setCenterLat(coords[0]);
            setCenterLong(coords[1]);
            setGetListingsRequest({latitude: coords[0], longitude: coords[1], radius:preferences.maxRadius, page:1});
        }
    }, [preferences, setCenterLat, setCenterLong, setGetListingsRequest, setUserPreferences]); //possibly get rid of additional dependencies
    
    //setting listings once they're fetched
    useEffect(() => {
        if (data) {
            setListingsPage(data);
        }
    }, [data, setListingsPage]);

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
                    <Filters refetch={refetch} listings={listingsPage.housingListings} setListings={setListingsPage} />
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
                    <div className="relative flex-grow min-w-0"><Map listings={listingsPage.housingListings} setRenderedListing={setRenderedListing} setIsModalUp={setIsListingModalUp}/></div>
                    <HousingSearch  isLoading={isLoading} isFetching={isFetching} isGrabbingFavorites = {isGettingFavorites} setRenderedListing={setRenderedListing} setIsModalUp={setIsListingModalUp}/>
                </span>
                <footer className="w-full border-t-2">
                    <Footer/>
                </footer>
            </main>
    )
}

export default Home;