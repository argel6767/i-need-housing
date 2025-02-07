import { getListingsInArea } from "@/endpoints/listings"
import { HouseListing } from "@/interfaces/entities"
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests"
import { useQuery } from "@tanstack/react-query"

export const useListings = (requestBody: GetListingsInAreaRequest) => {
    return useQuery({
        queryKey: ['listings', requestBody],
        queryFn: async ():Promise<Array<HouseListing>> => {
            return await getListingsInArea(requestBody);
        }
    })
}