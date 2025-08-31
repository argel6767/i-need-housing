import {HouseListing} from "@/interfaces/entities";

export interface ListingsResultsPageDto {
    housingListings: HouseListing[],
    pageNumber: number;
    totalPages: number
}