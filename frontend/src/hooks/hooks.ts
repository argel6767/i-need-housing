import { getFavoriteListings } from "@/endpoints/favorites"
import { getListingsInArea } from "@/endpoints/listings"
import { getUserPreferences } from "@/endpoints/preferences"
import { FavoriteListing, HouseListing, UserPreference } from "@/interfaces/entities"
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests"
import {useMutation, useQuery} from "@tanstack/react-query"
import {useGlobalContext} from "@/components/GlobalContext";
import {getProfilePicture, getProfilePictureURL, updateProfilePictureURL} from "@/endpoints/profilePictures";
import {useEffect} from "react";

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
export const useClearState = () => {
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
    const { data: url, isSuccess, isError, isLoading: urlLoading, refetch: refetchUrl } = useGetProfilePictureURL();

    const shouldFetchPicture = (isSuccess && url && (url !== "user does not have profile picture") && url !== "Called failed!");

    // First attempt to get picture
    const pictureQuery = useGetProfilePicture(url || '', {
        enabled: shouldFetchPicture && url.startsWith('http') // Extra safety check
    });

    // Check if we need to refresh URL
    const needsUrlRefresh = pictureQuery.isSuccess &&
        pictureQuery.data === "expired url";

    // Mutation to update URL when expired
    const updateUrlMutation = useMutation({
        mutationFn: updateProfilePictureURL,
        onSuccess: () => {
            // Refetch the URL after updating
            refetchUrl();
        }
    });

    // Auto-trigger URL refresh when expired
    useEffect(() => {
        if (needsUrlRefresh && !updateUrlMutation.isPending) {
            updateUrlMutation.mutate();
        }
    }, [needsUrlRefresh, updateUrlMutation]);

    // Return appropriate data based on whether user has profile picture
    if (!shouldFetchPicture) {
        return {
            data: null,
            isSuccess: true,
            isError: false,
            isLoading: urlLoading,
            isFetched: true,
            isFetching: false,
            isRefetching: false,
            isPending: false,
            hasProfilePicture: false,
            urlError: isError,
            isRefreshingUrl: false
        };
    }

    return {
        ...pictureQuery,
        hasProfilePicture: true,
        isLoading: urlLoading || pictureQuery.isLoading || updateUrlMutation.isPending,
        urlError: isError,
        isRefreshingUrl: updateUrlMutation.isPending
    };
};
