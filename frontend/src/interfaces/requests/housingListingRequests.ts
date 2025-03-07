import { HouseListing } from "../entities"

export interface GetListingsInAreaRequest {
    latitude: number,
    longitude: number,
    radius: number
}

export interface ExactPreferencesDTO {
    listings: HouseListing[],
    id: number
}