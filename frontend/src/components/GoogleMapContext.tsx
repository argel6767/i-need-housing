'use client'
import {createContext, useContext, useState, ReactNode, useMemo} from 'react';
import { LoadScript } from '@react-google-maps/api';
import { GOOGLE_API_KEY } from "@/utils/utils";
import {Loading} from "@/components/Loading";

// Define the libraries type
type Libraries = Array<"places" | "geometry" | "drawing">;
// All libraries your app needs
const libraries: Libraries = ['places', 'geometry', 'drawing'];

interface GoogleMapsContextType {
  isLoaded: boolean;
}

const GoogleMapsContext = createContext<GoogleMapsContextType | null>(null);

export const useGoogleMaps = () => {
  const context = useContext(GoogleMapsContext);
  if (!context) {
    throw new Error('useGoogleMaps must be used within a GoogleMapsProvider');
  }
  return context;
};

interface GoogleMapsProviderProps {
  children: ReactNode;
}

export const GoogleMapsProvider = ({ children }: GoogleMapsProviderProps) => {
  const [isLoaded, setIsLoaded] = useState(false);

  const contextValue = useMemo(() => ({isLoaded, setIsLoaded}), [isLoaded]);

  const handleLoad = () => {
    setIsLoaded(true);
    console.log("Google Maps API loaded successfully with all libraries");
  };


  return (
    <GoogleMapsContext.Provider value={contextValue}>
      <LoadScript
        googleMapsApiKey={GOOGLE_API_KEY ?? ""}
        libraries={libraries}
        onLoad={handleLoad}
        loadingElement={
        <div className={"flex justify-center items-center h-screen"}>
          <Loading loadingMessage={"Spinning up..."}/>
        </div>
        }
      >
        {children}
      </LoadScript>
    </GoogleMapsContext.Provider>
  );
};