'use client'
import { useRef, useState, useEffect } from "react";
import { useGlobalContext } from "@/components/GlobalContext";
import { PageTurner } from "../PageTurner";
import { useGoogleMaps } from "@/components/GoogleMapContext";

const LocationForm = () => {
    const { isLoaded } = useGoogleMaps();
    const { setNewUserInfo } = useGlobalContext();
    const [opportunityLocation, setOpportunityLocation] = useState<string>("");
    const [employerLocation, setEmployerLocation] = useState<string | null>(null);
    const [placePredictions, setPlacePredictions] = useState<google.maps.places.AutocompletePrediction[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const inputRef = useRef<HTMLInputElement>(null);
    const autocompleteService = useRef<google.maps.places.AutocompleteService | null>(null);
    const placesService = useRef<google.maps.places.PlacesService | null>(null);

    // Initialize services after API is loaded
    useEffect(() => {
        if (isLoaded && window.google) {
            autocompleteService.current = new google.maps.places.AutocompleteService();
            // Need a DOM element for PlacesService
            const dummyElement = document.createElement('div');
            placesService.current = new google.maps.places.PlacesService(dummyElement);
        }
    }, [isLoaded]);

    const getPlacePredictions = (input: string) => {
        if (!autocompleteService.current || !input) {
            setPlacePredictions([]);
            return;
        }
        
        setIsLoading(true);
        autocompleteService.current.getPlacePredictions(
            { input },
            (results, status) => {
                setIsLoading(false);
                if (status === google.maps.places.PlacesServiceStatus.OK && results) {
                    setPlacePredictions(results);
                } else {
                    setPlacePredictions([]);
                }
            }
        );
    };

    const handleEmployerLocation = (item: google.maps.places.AutocompletePrediction) => {
        setEmployerLocation(item.description);
        setNewUserInfo(prev => ({
            userType: prev.userType,
            newUserPreferencesDto: {
                ...prev.newUserPreferencesDto,
                jobLocationAddress: item.description
            }
        }));
        setPlacePredictions([]);
    };

    const initAutocomplete = () => {
        if (!isLoaded || !inputRef.current) return;
        
        const autocomplete = new google.maps.places.Autocomplete(inputRef.current);
        autocomplete.addListener('place_changed', () => {
            const place = autocomplete.getPlace();
            if (place.formatted_address) {
                handleOpportunityLocation(place);
            }
        });
    };

    useEffect(() => {
        if (isLoaded) {
            initAutocomplete();
        }
    }, [isLoaded]);

    const handleOpportunityLocation = (place: google.maps.places.PlaceResult) => {
        const city = place.formatted_address!;
        setOpportunityLocation(city);
        setNewUserInfo(prev => ({
            userType: prev.userType,
            newUserPreferencesDto: {
                ...prev.newUserPreferencesDto,
                cityOfEmploymentAddress: city
            }
        }));
    };

    if (!isLoaded) {
        return <div>Loading Google Maps API...</div>;
    }

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <div className="flex flex-col gap-5 bg-slate-100 rounded-xl min-h-96 shadow-xl py-3">
                <article className="flex flex-col justify-between h-full p-4">
                    <legend className="text-2xl font-semibold pb-2">Where is your new opportunity?</legend>
                    <span className="flex flex-col gap-4">
                        <label className="text-lg shadow-md">Where is your new opportunity located?
                            <input 
                                className="input input-bordered w-full" 
                                ref={inputRef} 
                                placeholder="ex. Cupertino, CA, USA"
                            />
                        </label>
                        <label className="text-lg shadow-md">Where is your employer located? (optional)
                            <input 
                                value={employerLocation ?? ""} 
                                className="input input-bordered w-full" 
                                placeholder="ex. Apple Headquarters, Apple Park Way, Cupertino, CA, USA" 
                                onChange={(evt) => {
                                    getPlacePredictions(evt.target.value);
                                    setEmployerLocation(evt.target.value);
                                }}
                            />
                        </label>
                        <span className="flex flex-col gap-3">
                            {placePredictions.map((item, index) => (
                                <button 
                                    key={index} 
                                    onClick={() => handleEmployerLocation(item)} 
                                    className="motion-preset-focus motion-duration-1000 hover:bg-slate-200 btn"
                                >
                                    {item.description}
                                </button>
                            ))} 
                        </span>
                    </span>
                </article>
                <nav className="flex justify-between mt-auto pt-4">
                    <PageTurner href="/new-user/user-type/" direction="left"/>
                    <PageTurner href="/new-user/max/" direction="right"></PageTurner>
                </nav>
            </div>
        </main>
    );
};

export default LocationForm;