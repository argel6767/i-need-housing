"use client"

import { useState } from "react";
import extend from "../../public/sidebar/sidebar-extend.svg"
import collapse from "../../public/sidebar/sidebar-collapse.svg"
import Image from "next/image";
import { HouseListing } from "@/interfaces/entities";


interface HousingSearchProps {
    city: string
    listings: HouseListing[]
}
export const HousingSearch = ({city, listings}):HousingSearchProps => {
    const [isOpen, setIsOpen] = useState<boolean>(true);
    return (
        <main className="relative h-full">
            {/* Wrapper div for button that follows sidebar animation */}
            <div className={`fixed top-1/2 -translate-y-1/2 transform transition-transform duration-300 ease-in-out ${
                isOpen ? 'right-[calc(18rem)] md:right-[calc(25rem)] lg:right-[calc(35rem)] xl:right-[calc(40rem)]' : 'right-0'
            } z-50`}>
                <button
                    className="p-2 text-white rounded hover:bg-gray-700"
                    onClick={() => setIsOpen(!isOpen)}
                >  {/*Vectors and icons by <a href="https://github.com/primer/octicons?ref=svgrepo.com" target="_blank">Primer</a> in MIT License via <a href="https://www.svgrepo.com/" target="_blank">SVG Repo</a> */}
                    {isOpen ? 
                        <Image src={collapse} alt="Close" width={40}/> : 
                    <Image src={extend} alt="Open" width={40}/>
                    }
                </button>
            </div>

            <div 
                className={`absolute top-0 right-0 w-[18rem] md:w-[25rem] lg:w-[35rem] xl:w-[40rem] h-full bg-slate-50 transform transition-transform duration-300 ease-in-out ${
                    isOpen ? 'translate-x-0' : 'translate-x-full'
                } z-40 shadow-lg rounded-l-lg`}
            >
                <div className="p-4 ">
                    <h2 className="text-xl font-bold mb-4">View Listings in {city}</h2>
                    <nav>
                        <ul className="space-y-2">
                        </ul>
                    </nav>
                </div>
            </div>

            
        </main>
    );
};