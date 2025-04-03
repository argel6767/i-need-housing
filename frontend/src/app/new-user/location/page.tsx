'use client'
import Autocomplete, { usePlacesWidget } from "react-google-autocomplete";



const LocationForm = () => {
    const KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY

    const {ref} = usePlacesWidget({
        apiKey:KEY,
        onPlaceSelected: (place) => console.log(place)
    })
    
    return (
        <main>
            <input className="input input-bordered w-full max-w-xs" ref={ref}/>
        </main>
    )
}

export default LocationForm;