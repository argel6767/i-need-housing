"use client"

import {useEffect, useState} from "react";
import extend from "../../public/sidebar/sidebar-extend.svg"
import collapse from "../../public/sidebar/sidebar-collapse.svg"
import Image from "next/image";
import {  HouseListing } from "@/interfaces/entities";
import { HousingCard } from "./HousingsList";
import { GroupOfSkeletons, SkeletonText } from "./Loading";
import {useHomeContext} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {useGlobalContext} from "@/components/GlobalContext";
import {ListingsResultsPageDto} from "@/interfaces/responses/listingsResponses";

interface PaginationButtonProps {
    page: number | string;
    currentPage: number;
}

const PaginationButton = ({page, currentPage}: PaginationButtonProps) => {
    const {setListingsPage, setGetListingsRequest} = useHomeContext();
    const isDisabled = () => {
        return page === "..."
    }
    const isCurrentPage = () => {
        return page === currentPage;
    }

    const onClick = () => {
        const pageNumber = Number(page);

        // Update both the listings page state AND the request
        setListingsPage((prevState) => ({...prevState, pageNumber: pageNumber}));
        setGetListingsRequest((prev) => {
            if (!prev) {
                return prev;
            }
            return {...prev, page: pageNumber}
        });
    }

    return (
        <button onClick={onClick} disabled={isDisabled()} className={`join-item btn ${isCurrentPage()? "bg-slate-300" : "bg-slate-200"}`}>{page}</button>
    )
}

interface HousingSearchFooterProps {
    listingsPage: ListingsResultsPageDto
}

const getPageNumbers = (current: number, total: number) => {
    const pages: Array<number | string> = [];
    if (total <= 6) {
        for (let i = 1; i <= total; i++) pages.push(i);
    } else {
        pages.push(1);
        if (current > 4) {
            pages.push("...");
        }
        const start = Math.max(2, current - 2);
        const end = Math.min(total - 1, current + 2);

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }
        if (current < total - 3) {
            pages.push("...");
        }
        pages.push(total);
    }
    return pages;
};

const HousingSearchFooter = ({listingsPage}: HousingSearchFooterProps) => {
    const pages = getPageNumbers(listingsPage.pageNumber, listingsPage.totalPages);

        return(
            <div className={"join"}>
                {pages.map((page, idx) => (
                   <PaginationButton
                        key={idx}
                        page={page}
                        currentPage={listingsPage.pageNumber}
                   />
                ))}
            </div>
        )

}

interface HousingSearchProps {
    isLoading: boolean
    isFetching: boolean
    isGrabbingFavorites: boolean
    setRenderedListing: React.Dispatch<React.SetStateAction<HouseListing | undefined>>
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>,
}

/**
 * Side container that holds all Listings
 * @param param
 * @returns 
 */
export const HousingSearch = ({ isLoading, isFetching, isGrabbingFavorites, setRenderedListing, setIsModalUp}:HousingSearchProps) => {
    const [isOpen, setIsOpen] = useState<boolean>(true);
    const {isResetting, isFiltering, listingsPage} = useHomeContext();
    const {userPreferences} = useGlobalContext();

    const isListingsChanging = () => {
        return isFetching || isLoading || isGrabbingFavorites || isResetting || isFiltering || !listingsPage || !listingsPage.housingListings || !listingsPage.totalPages || !listingsPage.pageNumber;
    }

    return (
        <main className="relative h-full">
            {/* Wrapper div for button that follows sidebar animation */}
            <div className={`fixed top-1/2 -translate-y-1/2 transform transition-transform duration-300 ease-in-out ${
                isOpen ? 'right-[calc(18rem)] md:right-[calc(25rem)] lg:right-[calc(35rem)] xl:right-[calc(40rem)]' : 'right-0'} z-30`}>
                <button
                    className="p-2 text-white rounded hover:bg-gray-700"
                    onClick={() => setIsOpen(!isOpen)}
                >  {/*Vectors and icons by <a href="https://github.com/primer/octicons?ref=svgrepo.com" target="_blank">Primer</a> in MIT License via <a href="https://www.svgrepo.com/" target="_blank">SVG Repo</a> */}
                    {isOpen ? 
                        <Image src={collapse} alt="Close" width={40}/> : 
                    <Image src={extend} alt="Open" width={40}/>
                    }
                </button>
            </div>
            <div 
                className={`absolute top-0 right-0 w-[18rem] md:w-[25rem] lg:w-[35rem] xl:w-[40rem] h-full bg-slate-50 transform transition-transform duration-300 ease-in-out ${
                    isOpen ? 'translate-x-0' : 'translate-x-full'} z-30 shadow-lg rounded-l-lg overflow-y-scroll`}>
                <div className="p-4">
                    <h2 className="text-3xl font-bold text-center p-4 flex justify-center items-center">
                        {isListingsChanging() ?
                            <SkeletonText/> : "View Listings Around " + userPreferences?.cityOfEmployment}
                    </h2>
                    <nav className="flex flex-col justify-center w-full overflow-y-auto">
                        <ul className="grid grid-cols-1 md:grid-cols-2 gap-3 p-4">
                            {isListingsChanging() || isLoading || isGrabbingFavorites ?
                                <GroupOfSkeletons numOfSkeletons={8}/> :
                                listingsPage.housingListings
                                    .filter((listing) => (listing.coordinates !== null))
                                    .map((listing) => (
                                        <HousingCard key={listing.id} listing={listing} setIsModalUp={setIsModalUp}
                                                     setRenderedListing={setRenderedListing}/>
                                    ))}
                        </ul>
                    </nav>
                    <div className={"shrink-0 pt-4 pb-2 border-t bg-slate-50 sticky bottom-0 z-10 flex justify-center items-center"}>
                        <HousingSearchFooter listingsPage={listingsPage}/>
                    </div>
                </div>
            </div>
        </main>
    );
};