export interface RawCoordinateUserPreferenceDto {
    jobLocationCoordinates?: Array<number>,
    cityOfEmploymentCoordinates?: Array<number>,
    maxRadius?: number,
    maxRent?: number,
    travelType?: number,
    bedrooms?: number,
    bathrooms?: number,
    isFurnished?: boolean,
    startDate?: Date,
    endDate?: Date
}
