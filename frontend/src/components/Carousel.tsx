import boston from "../../public/carousel/boston.jpg"
import san_fran from "../../public/carousel/san_fran.jpg"
import new_york from "../../public/carousel/new_york.jpg"
import dallas from "../../public/carousel/dallas.jpg"
import { Card } from "./Card"

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