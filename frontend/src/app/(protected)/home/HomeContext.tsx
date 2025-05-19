'use client'

import {createContext, useContext, useState, useMemo, ReactNode} from "react";
import {Loading} from "@/components/Loading";

interface HomeContextType {
    filterRendered: ReactNode,
    setFilterRendered: React.Dispatch<React.SetStateAction<ReactNode>>,
    isFiltersChanged: boolean,
    setIsFiltersChanged: React.Dispatch<React.SetStateAction<boolean>>,
    isListingsFiltered: boolean,
    setIsListingsFiltered: React.Dispatch<React.SetStateAction<boolean>>,
}

const HomeContext = createContext<HomeContextType | undefined>(undefined)

interface HomeProviderProps {
    children: React.ReactNode;
}

/**
 * Houses Filter component state variables
 */

export const HomeProvider = ({ children }: HomeProviderProps) => {
    const [filterRendered, setFilterRendered] = useState<ReactNode>(<div><Loading loadingMessage={"Rendering Filter..."}/></div>);
    const [isFiltersChanged, setIsFiltersChanged] = useState<boolean>(false);
    const [isListingsFiltered, setIsListingsFiltered] = useState<boolean>(false);

    const contextValues = useMemo(() => ({
        filterRendered, setFilterRendered, isFiltersChanged, setIsFiltersChanged, isListingsFiltered, setIsListingsFiltered,
    }), [filterRendered, setFilterRendered, isFiltersChanged]);

    return (
        <HomeContext.Provider value={contextValues}>
            {children}
        </HomeContext.Provider>
    )
}

export const useHomeContext = (): HomeContextType => {
    const context = useContext(HomeContext);
    if (!context) {
        throw new Error('useHomeContext must be used within HomeContext');
    }
    return context;
}