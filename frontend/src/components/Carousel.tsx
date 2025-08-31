"use client"
import boston from "../../public/carousel/boston.jpg"
import san_fran from "../../public/carousel/san_fran.jpg"
import new_york from "../../public/carousel/new_york.jpg"
import dallas from "../../public/carousel/dallas.jpg"
import { Card } from "./Card"
import { useState } from "react"
import { ChevronLeft, ChevronRight } from "lucide-react"

/**
 * Carousel component that houses city cards for landing page
 * @returns 
 */

export const Carousel = () => {
    const cards = [
        {
            image: boston,
            alt: "Boston",
            body: "Boston",
            credit: (
                <footer className="text-sm">
                    Photo by <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/@iam_aspencer?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Andrew Spencer</a> on <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/photos/brown-concrete-building-during-daytime-hoTDnbNRyRY?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                </footer>
            ),
        },
        {
            image: new_york,
            alt: "New York",
            body: "New York",
            credit: (
                <footer className="text-sm">
                    Photo by <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/@timovaknar?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Timo Wagner</a> on <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/photos/empire-state-building-new-york-at-night-fT6-YkB0nfg?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                </footer>
            ),
        },
        {
            image: san_fran,
            alt: "San Francisco",
            body: "San Francisco",
            credit: (
                <footer className="text-sm">
                    Photo by <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/@petearoner?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Peter Aroner</a> on <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/photos/red-and-white-bridge-over-the-trees-FtCHWeM2aWI?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                </footer>
            ),
        },
        {
            image: dallas,
            alt: "Dallas",
            body: "Dallas",
            credit: (
                <footer className="text-sm">
                    Photo by <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/@andreasdress?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Andreas Rasmussen</a> on <a className={"hover:underline hover:underline-offset-4"} href="https://unsplash.com/photos/timelapse-photography-of-red-and-orange-light-crossing-on-road-near-city-during-nighttime-BOtW7WkuDds?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                </footer>
            ),
        },
    ];

    // Duplicate cards for seamless loop
    const carouselCards = [...cards, ...cards];

    return (
        <main className="py-5 flex justify-center px-2">
            <div className="relative w-full max-w-6xl hidden md:block md:h-[400px] lg:h-[450px] overflow-hidden rounded-box bg-transparent mb-8">
                <div
                    className="flex items-center gap-8 animate-carousel"
                    style={{
                        width: `${carouselCards.length * 320}px`, // 300px card + 20px gap
                        height: "100%",
                    }}
                >
                    {carouselCards.map((card, idx) => (
                        <div
                            key={idx}
                            className="w-[300px] hidden md:block md:h-[400px] lg:h-[450px] flex-shrink-0 flex flex-col justify-start items-center overflow-visible"
                        >
                            <Card
                                image={card.image}
                                alt={card.alt}
                                children={card.credit}
                                body={card.body}
                            />
                        </div>
                    ))}
                </div>
                <style jsx>{`
                    @keyframes carousel {
                        0% {
                            transform: translateX(0);
                        }
                        100% {
                            transform: translateX(-50%);
                        }
                    }
                    .animate-carousel {
                        animation: carousel 20s linear infinite;
                    }
                `}</style>
            </div>
        </main>
    );
};

interface ArrowImageCarouselProps  {
    images:string[] | undefined
}

/**
 * A carousel that renders an image at a time with arrows on both side to choose which to render
 * @param param
 */
export const ArrowImageCarousel = ({images = []}:ArrowImageCarouselProps) => {

    const numOfImages = images?.length ?? 0;

    //no images so placeholder is needed
    if (images.length == 0) {
        images.push("./placeholder.jpg");
    }

    const [currentIndex, setCurrentIndex] = useState<number>(0);

    const goToPrevious = () => {
        const isFirstImage = currentIndex === 0;
        const newIndex = isFirstImage ? numOfImages - 1 : currentIndex - 1;
        setCurrentIndex(newIndex);
    };
    
    const goToNext = () => {
        const isLastImage = currentIndex === numOfImages - 1;
        const newIndex = isLastImage ? 0 : currentIndex + 1;
        setCurrentIndex(newIndex);
    };
    
    return (
        <>
        <div className="relative w-full max-w-2xl mx-auto p-1 flex justify-center">
        <div className="w-10/12 h-80 rounded-lg shadow-lg">
            <img 
            src={images[currentIndex]} 
            alt={`Slide ${currentIndex + 1}`}
            className="w-full h-full object-cover rounded-xl" 
            />
        </div>
        
        {/* Left Arrow */}
        <button onClick={goToPrevious} className="absolute left-2 top-1/2 -translate-y-1/2 bg-white  rounded-full p-2 hover:bg-opacity-75 transition-all" disabled={numOfImages === 0}>
            <ChevronLeft size={24} />
        </button>
        
        {/* Right Arrow */}
        <button onClick={goToNext} className="absolute right-2 top-1/2 -translate-y-1/2 bg-white rounded-full p-2 hover:bg-opacity-75 transition-all" disabled={numOfImages === 0}>
            <ChevronRight size={24} />
        </button>

        </div>
        {/* Dots Indicator */}
        <div className="flex justify-center space-x-2 py-3">
            {images.map((_, index) => (
            <button key={index} onClick={() => setCurrentIndex(index)} className={`w-3 h-3 rounded-full ${index === currentIndex ? "bg-white" : "bg-gray-400"} mt-2`}/>
        ))}
        </div>
        </>
        
        
    );
};