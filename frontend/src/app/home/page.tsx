"use client"

import { Footer } from "@/components/Footer";
import { HousingSearch } from "@/components/HousingSearch";
import { Map } from "@/components/Map";
import { LoggedInNavBar } from "@/components/Navbar";
import { useListings } from "@/hooks/hooks";

const Home = () => {
    const [requestBody, setRequestBody] = useState<Get
    const {isLoading, error, data} = useListings(requestBody);
    return (
        <main className="flex flex-col h-screen">
            <nav className="">
                <LoggedInNavBar/>
            </nav>
            <span className="flex relative flex-1 w-full rounded-lg py-2 overflow-x-hidden">
                <div className="relative flex-grow min-w-0"><Map listings={[]}/></div>
                <HousingSearch/>
            </span>
            <div className="w-full border-t-2">
                <Footer/>
            </div>
        </main>
        
    )
}

export default Home;