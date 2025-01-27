"use client"

import { Footer } from "@/components/Footer";
import { HousingSearch } from "@/components/HousingSearch";
import { Map } from "@/components/Map";
import { LoggedInNavBar } from "@/components/Navbar";

const Home = () => {
    return (
        <main className="flex flex-col h-screen">
            <nav className="py-3">
                <LoggedInNavBar/>
            </nav>
            <span className="h-5/6">
                <Map/> 
            </span>
            <div className="w-full border-t-2">
                <Footer/>
            </div>
        </main>
        
    )
}

export default Home;