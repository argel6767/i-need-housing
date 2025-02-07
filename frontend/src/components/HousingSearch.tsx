"use client"

import { useState } from "react";
import extend from "../../public/sidebar/sidebar-extend.svg"
import collapse from "../../public/sidebar/sidebar-collapse.svg"
import Image from "next/image";


interface HousingSearchProps {
    city: string
    
}
export const HousingSearch = ({city}):HousingSearchProps => {
    const [isOpen, setIsOpen] = useState<boolean>(true);
    return (
        <main className="relative h-full">
            {/* Wrapper div for button that follows sidebar animation */}
            <div className={`fixed top-1/2 -translate-y-1/2 transform transition-transform duration-300 ease-in-out ${
                isOpen ? 'right-64' : 'right-0'
            } z-50`}>
                <button
                    className="p-2 text-white rounded hover:bg-gray-700"
                    onClick={() => setIsOpen(!isOpen)}
                >  {/*Vectors and icons by <a href="https://github.com/primer/octicons?ref=svgrepo.com" target="_blank">Primer</a> in MIT License via <a href="https://www.svgrepo.com/" target="_blank">SVG Repo</a> */}
                    {isOpen ? 
                        <Image src={collapse} alt="Close" width={30}/> : 
                    <Image src={extend} alt="Open" width={30}/>
                    }
                </button>
            </div>

            <div 
                className={`absolute top-0 right-0 w-64 h-full bg-slate-50 transform transition-transform duration-300 ease-in-out ${
                    isOpen ? 'translate-x-0' : 'translate-x-full'
                } z-40 shadow-lg rounded-l-lg`}
            >
                <div className="p-4 ">
                    <h2 className="text-xl font-bold mb-4">View Listings</h2>
                    <nav>
                        <ul className="space-y-2">
                        </ul>
                    </nav>
                </div>
            </div>

            
        </main>
    );
};