package com.ineedhousing.new_listings_service.models.events.new_listings;

public sealed interface NewListingsCollectionEvent permits AirbnbCollectionEvent, RentCastCollectionEvent, ZillowCollectionEvent {
}
