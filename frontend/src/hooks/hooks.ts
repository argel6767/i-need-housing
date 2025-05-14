import { getFavoriteListings } from "@/endpoints/favorites"
import { getListingsInArea } from "@/endpoints/listings"
import { getUserPreferences } from "@/endpoints/preferences"
import { FavoriteListing, HouseListing, UserPreference } from "@/interfaces/entities"
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests"
import { useQuery } from "@tanstack/react-query"
import { useEffect} from "react";
import {checkCookie} from "@/endpoints/auths";
import {useGlobalContext} from "@/components/GlobalContext";

// fetches listings
export const useListings = (requestBody: GetListingsInAreaRequest | null, options?: { enabled?: boolean }) => {
    return useQuery({
        queryKey: ['listings', requestBody],
        queryFn: async ():Promise<Array<HouseListing>> => {
            return await getListingsInArea(requestBody);
        },
        enabled: options?.enabled ?? !!requestBody
    })
}

//fetches user's preferences
export const useUserPreferences = () => {
    return useQuery({
        queryKey: ['userPreferences'],
        queryFn: async ():Promise<UserPreference> => {
            return await getUserPreferences();
        }
    })
}

//fetches favorite listings
export const useFavoriteListings = () => {
    return useQuery({
        queryKey: ['favoriteListings'],
        queryFn: async ():Promise<FavoriteListing[]> => {
            return await getFavoriteListings();
        }
    })
}

export const useCheckCookie = async () => {
    const cookieStatus = await checkCookie();
    return cookieStatus === "Token is still valid"; //TODO Finish this!!!
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