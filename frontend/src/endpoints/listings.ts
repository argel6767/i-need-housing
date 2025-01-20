import { apiClient } from "./apiConfig";
import { GetListingsInAreaRequest } from "@/interfaces/requests/housingListingRequests";
import { HouseListing } from "@/interfaces/entities";

const MODULE_MAPPING = "/listings"

/**
 * holds Housing listings api calls
 */

export const getListingsInArea = async(requestBody: GetListingsInAreaRequest): Promise<
