export interface User {
    id: number;
    email: string;
    passwordHash: string,
    verificationCode: string,
    codeExpiry?: Date,
    isEnabled: boolean,
    authorities?: string,
    userType?: string,
    userPreference: UserPreference,
    favoriteListings: Array<FavoriteListing>
    lastLogin?: Date,
    createdAt: Date
}

export interface UserPreference {
    id: number,
    jobLocation: Array<number>,
    cityOfEmployment: Array<number>,
    desiredArea: Array<Object>,
    maxRadius: number,
    maxRent: number,
    travelType?: string,
    minNumberOfBedRooms: number,
    minNumberOfBathrooms: number,
    isFurnished: boolean,
    internshipStart: Date,
    internshipEnd: Date,
    updatedAt: Date
}

export interface HouseListing {
    id: number,
    source: string,
    title: string,
    description: string,
    rate: number,
    coordinates: Array<Number>
    address: string,
    listingUrl: string,
    imageUrls: Array<string>,
    propertyType?: string,
    numBeds?: number,
    numBaths?: number,
    isPetFriendly?: boolean,
    isFurnished?: boolean
}

export interface FavoriteListing {
    id: number,
    user: User,
    housingListing: HouseListing,
    createdAt: Date
}