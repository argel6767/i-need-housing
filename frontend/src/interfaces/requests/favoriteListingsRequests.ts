import { HouseListing } from "../entities";

export interface AddFavoriteListingsRequest {
    listings: Array<HouseListing>
}

export interface DeleteFavoriteListingRequest {
    listings: Array<HouseListing>
}