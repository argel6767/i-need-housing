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
    return(
        <main className="py-5 flex justify-center h-1/2">
            <div className="carousel carousel-center rounded-box gap-8 py-4 ">
                <Card image={boston} alt={"Boston"} children={<footer className="text-sm">Photo by <a href="https://unsplash.com/@iam_aspencer?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Andrew Spencer</a> on <a href="https://unsplash.com/photos/brown-concrete-building-during-daytime-hoTDnbNRyRY?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                    </footer>} body={"Boston"}/>
                <Card image={new_york} alt={"New York"} children={<footer className="text-sm">Photo by <a href="https://unsplash.com/@timovaknar?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Timo Wagner</a> on <a href="https://unsplash.com/photos/empire-state-building-new-york-at-night-fT6-YkB0nfg?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                    </footer>} body="New York"/>
                <Card image={san_fran} alt={"San Francisco"} children={<footer className="text-sm">Photo by <a href="https://unsplash.com/@petearoner?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Peter Aroner</a> on <a href="https://unsplash.com/photos/red-and-white-bridge-over-the-trees-FtCHWeM2aWI?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                    </footer>} body="San Francisco"/>
                <Card image={dallas} alt="Dallas" children={<footer className="text-sm">Photo by <a href="https://unsplash.com/@andreasdress?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Andreas Rasmussen</a> on <a href="https://unsplash.com/photos/timelapse-photography-of-red-and-orange-light-crossing-on-road-near-city-during-nighttime-BOtW7WkuDds?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
                    </footer>} body="Dallas"/> 
            </div>
        </main>
    )
}

interface ArrowImageCarouselProps  {
    images:string[] | undefined
}

/**
 * A carousel that renders an image at a time with arrows on both side to choose which to render
 * @param param
 */
export const ArrowImageCarousel = ({images}:ArrowImageCarouselProps) => {

    const numOfImages = images?.length;

    if(numOfImages == 0) {
        images = ["./placeholder.jpg"]
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
            <button key={index} onClick={() => setCurrentIndex(index)} className={`w-3 h-3 rounded-full ${index === currentIndex ? "bg-white" : "bg-gray-400"}`}/>
        ))}
        </div>
        </>
        
        
    );
};