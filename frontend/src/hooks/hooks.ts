import { getFavoriteListings } from "@/endpoints/favorites"
import {filterListingsByPreferencesV2, getListingsInArea, getListingsInAreaV2} from "@/endpoints/listings"
import { getUserPreferences } from "@/endpoints/preferences"
import { FavoriteListing, HouseListing, UserPreference } from "@/interfaces/entities"
import {GetListingsInAreaRequest, GetListingsInAreaRequestV2} from "@/interfaces/requests/housingListingRequests"
import { useQuery} from "@tanstack/react-query"
import {useGlobalContext} from "@/components/GlobalContext";
import {getProfilePicture, getProfilePictureURL} from "@/endpoints/profilePictures";
import {useEffect, useState} from "react";
import {useHomeContext} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {useExistingUserContext} from "@/app/(protected)/(existing_user)/ExistingUserContext";
import {DEFAULT_PROFILE_PICTURE_URL} from "@/utils/utils";
import {useProtectedContext} from "@/app/(protected)/ProtectedRoute";
import {ListingsResultsPageDto} from "@/interfaces/responses/listingsResponses";

// fetches listings
export const useGetListings = (requestBody: GetListingsInAreaRequest | null, options?: { enabled?: boolean }) => {
    return useQuery({
        queryKey: ['listings', requestBody],
        queryFn: async ():Promise<Array<HouseListing>> => {
            return await getListingsInArea(requestBody);
        },
        enabled: options?.enabled ?? !!requestBody
    })
}

export const useGetListingsV2 = (requestBody: GetListingsInAreaRequestV2 | null, options?: { enabled?: boolean }) => {
    return useQuery({
        queryKey: ['listings, page: ' + requestBody?.page, requestBody],
        queryFn: async ():Promise<ListingsResultsPageDto> => {
            return await getListingsInAreaV2(requestBody);
        },
        enabled: options?.enabled ?? !!requestBody
    })
}

export const useGetFilteredListings= (page: number) => {
    return useQuery({
        queryKey: ['listings, page: ', page],
        queryFn: async ():Promise<ListingsResultsPageDto> => {
            return await filterListingsByPreferencesV2(page)
        }
    })
}

//fetches user's preferences
export const useGetUserPreferences = () => {
    return useQuery({
        queryKey: ['userPreferences'],
        queryFn: async ():Promise<UserPreference> => {
            return await getUserPreferences();
        }
    })
}

//fetches favorite listings
export const useGetFavoriteListings = () => {
    return useQuery({
        queryKey: ['favoriteListings'],
        queryFn: async ():Promise<FavoriteListing[]> => {
            return await getFavoriteListings();
        }
    })
}


//sets all global state values to their default values
export const useClearGlobalContext = () => {
    const {setCenterLat, setCenterLong, setUser, setUserPreferences, setFavoriteListings, setNewUserInfo, setIsIntern, setIsFirstTimeUser} = useGlobalContext();
    return () => {
        setCenterLat(0.0);
        setCenterLong(0.0);
        setUser(null);
        setUserPreferences(null);
        setFavoriteListings([]);
        setNewUserInfo({userType: '', newUserPreferencesDto: {}})
        setIsIntern(false);
        setIsFirstTimeUser(false)
    }
}

// sets all home context state values to their default values
export const useClearHomeContext = () => {
    const {setListingsPage, setIsListingsFiltered, setIsFiltersChanged, setIsFiltering, setIsFilterModalUp,
        setIsListingModalUp, setIsResetting, setFilterRendered, setInitialPreferences, setIsSaving} = useHomeContext();

    return () => {
        setIsSaving(false);
        setIsFiltering(false);
        setIsFilterModalUp(false);
        setIsFiltersChanged(false);
        setIsResetting(false);
        setIsListingsFiltered(false);
        setIsListingModalUp(false);
        setListingsPage({housingListings: [], pageNumber: 1, totalPages: 1})
        setFilterRendered(null)
        setInitialPreferences(undefined);
    }
}

export const useClearExistingUserContext = () => {
    const {setProfilePictureUrl} = useExistingUserContext();
    return () => {
        setProfilePictureUrl(DEFAULT_PROFILE_PICTURE_URL);
    }
}
export const useClearProtectedContext = () => {
    const {setIsAuthLoading, setIsAuthorized} = useProtectedContext();
    return () => {
        setIsAuthLoading(true);
        setIsAuthorized(false);
    }
}

//calculates radius in meters TODO this will be used for polygon drawing, should it be implemented
export const useFindRadiusInMeters = () => {
    const METERS_IN_A_MILE = 1609.344
    const {userPreferences} = useGlobalContext();
    let totalMeters;
    if (userPreferences) {
        totalMeters = userPreferences?.maxRadius * METERS_IN_A_MILE;
    }
    return Math.floor(totalMeters!);
}

/**
 * fetch url
 */
export const useGetProfilePictureURL = () => {
    return useQuery({
        queryKey: ['profilePictureURL'],
        queryFn: async ():Promise<string> => {
            return await getProfilePictureURL();
        }
    })
}


/**
 * fetch actual picture
 * @param url
 * @param options - React Query options like enabled, retry, etc.
 */
export const useGetProfilePicture = (url: string, options = {}) => {
    return useQuery({
        queryKey: ['profilePicture', url],
        queryFn: async (): Promise<string> => {
            return await getProfilePicture(url);
        },
        ...options // Spread the options to allow enabled, retry, etc.
    })
}

/**
 * does the entire step by step flow of grabbing a users profile picture
 */
export const useProfilePictureWithURL = () => {
    const [isLoaded, setIsLoaded] = useState<boolean>(false);
    const [isFailed, setIsFailed] = useState<boolean>(false);
    const [profilePicUrl, setProfilePicUrl] = useState<string>("")

    useEffect(() => {
        const fetchProfilePicture = async () => {
            try {
                const url = await getProfilePictureURL();

                // ✅ Fixed string comparison
                if (url === "user does not have profile picture" || url === "Call failed") {
                    setIsFailed(true);
                } else {
                    const profilePic = await getProfilePicture(url);
                    setProfilePicUrl(profilePic);
                }
            } catch (error) {
                setIsFailed(true);
            } finally {
                setIsLoaded(true); // ✅ Always set loaded at the end
            }
        }
        fetchProfilePicture()
    }, []);

    return {
        profilePicUrl,
        isFailed,
        isLoaded
    };
};


export const usePingServer = () => {
    const backendUrl = process.env.NEXT_PUBLIC_BACKEND_API_BASE_URL;

    useEffect(() => {
        // Only run in browser
        if (typeof window !== "undefined") {
            const hasAlreadyPinged = sessionStorage.getItem("hasAlreadyPinged");
            if (hasAlreadyPinged === null || hasAlreadyPinged === "false") {
                fetch(backendUrl + "ping");
                sessionStorage.setItem("hasAlreadyPinged", "true");
            }
        }
    }, [backendUrl]);
};