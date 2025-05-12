import { getFavoriteListings } from "@/endpoints/favorites"
import { getListingsInArea } from "@/endpoints/listings"
import { getUserPreferences } from "@/endpoints/preferences"
import { FavoriteListing, HouseListing, UserPreference } from "@/interfaces/entities"
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests"
import { useQuery } from "@tanstack/react-query"

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

export const useFavoriteListings = () => {
    return useQuery({
        queryKey: ['favoriteListings'],
        queryFn: async ():Promise<FavoriteListing[]> => {
            return await getFavoriteListings();
        }
    })
}

export const useCheckCookie = async () => {

}