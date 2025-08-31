import { HouseListing } from "../entities"

export interface GetListingsInAreaRequest {
    latitude: number,
    longitude: number,
    radius: number
}

export interface GetListingsInAreaRequestV2 {
    latitude: number,
    longitude: number,
    radius: number
    page: number
}

export interface ExactPreferencesDTO {
    listings: HouseListing[],
    id: number
}