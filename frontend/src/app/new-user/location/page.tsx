'use client'
import { useRef, useState } from "react";
import  { usePlacesWidget } from "react-google-autocomplete";
import usePlacesService from "react-google-autocomplete/lib/usePlacesAutocompleteService";
import { PageTurner } from "../PageTurner";
import { useGlobalContext } from "@/components/GlobalContext";

const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY

/**
 * Where users put in their employment location
 * @returns 
 */
const LocationForm = () => {
    const {newUserPreferencesDto, setNewUserPreferencesDto} = useGlobalContext();
    const [opportunityLocation, setOpportunityLocation] = useState<string>("");
    const [employerLocation, setEmployerLocation] = useState<string | null>(null)
    const {placePredictions, getPlacePredictions, isPlacePredictionsLoading,} = usePlacesService({apiKey: KEY,}); //places out complete

    //both below update fields of global user preference dto object 
    const handleEmployerLocation = (item:google.maps.places.AutocompletePrediction) => {
        setEmployerLocation(item.description);
        setNewUserPreferencesDto((prev) => (
        {...prev,jobLocationAddress: item.description}))
    }

    const handleOpportunityLocation = (place:google.maps.places.PlaceResult) => {
        const city = place.formatted_address!
        setOpportunityLocation(city);
        setNewUserPreferencesDto((prev) => (
            {...prev, cityOfEmployment:city}
        ));
    }

    const {ref} = usePlacesWidget({apiKey:KEY, onPlaceSelected: (place) => handleOpportunityLocation(place)}) //ref for places widget

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <div className="flex flex-col gap-5 bg-slate-100 rounded-xl min-h-96 shadow-xl py-3">
            <article className="flex flex-col justify-between h-full p-4">
            <legend className="text-2xl font-semibold pb-2">Where is your new opportunity?</legend>
                <span className="flex flex-col gap-4">
                    <label className="text-lg shadow-md">Where is your new opportunity located?
                        <input className="input input-bordered w-full" ref={ref} placeholder="ex. Cupertino, CA, USA"/>
                    </label>
                    <label className="text-lg shadow-md">Where is your employer located? (optional)
                        <input value={!employerLocation ? undefined : employerLocation} className="input input-bordered w-full" placeholder="ex. Apple Headquarters, Apple Park Way, Cupertino, CA, USA" onChange={(evt) => {getPlacePredictions({ input: evt.target.value });}}/>
                    </label>
                    <span className="flex flex-col gap-3">
                    {placePredictions.map((item, index) => (
                    <button key={index} onClick={() => handleEmployerLocation(item)} className="motion-preset-focus motion-duration-1000 hover:bg-slate-200 btn">{item.description}</button>))} 
                    </span>
                </span>
            </article>
            <nav className="flex justify-end mt-auto pt-4">
                <PageTurner href="/new-user/rent" direction="right"></PageTurner>
            </nav>
            </div>
        </main>
    )
}

export default LocationForm;