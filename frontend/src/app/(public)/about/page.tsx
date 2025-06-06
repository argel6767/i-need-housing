
import { Footer } from "@/components/Footer";
import { MobileNavbar, Navbar } from "@/components/Navbar";
import {Metadata} from "next";

export const metadata: Metadata = {
    title: "About | INeedHousing",
    description: "INeedHousing About Section",
}

const About = () => {
    return (
        <main className="h-screen flex flex-col justify-between gap-4">
            <div className="block md:hidden">
                <MobileNavbar/>
            </div>
            <div className="hidden md:block">
                <Navbar/>
            </div>
            <article id="about" className="px-4 space-y-2">
                <h1 className="text-3xl font-semibold italic">About Us</h1>
                <p>INeedHousing was conjured and developed by Argel Hernandez Amaya (me) as a platform to ease the difficulty of finding housing, whether it be temporary or permanent.
                    By leveraging multiple third parties services, such as RentCast, Airbnb, and Zillow, INeedHousing can be used as an aggregate, allowing ease of access for users.
                </p>
            </article>
            <article id="contact" className="px-4 space-y-2">
            <h1 className="text-3xl font-semibold italic">Contact Us</h1>
                <p>Any messages/questions about me or INeedHousing can be sent over to INeedHousing's email <a className="hover:underline font-semibold" href="mailto:ineedhousing.com@gmail.com">ineedhousing.com@gmail.com</a>, my personal email <a className="hover:underline font-semibold" href="mailto:argel6767@gmail.com">argel6767@gmail.com</a>, or to my personal <a className="hover:underline font-semibold" href="www.linkedin.com/in/argel-hernandez-amaya">LinkedIn</a>.
                </p>
            </article>
            <article id="contribution" className="px-4 space-y-2">
                <h1 className="text-3xl font-semibold italic">Contributing</h1>
                <p>As of right now no outside contribution is needed. However, I am always happy to take suggestions and/or potential improvements to the site's visuals and functionalities.</p>
            </article>
            <div className="border-t-2">
               <Footer/> 
            </div>
            
        </main>
    )
}

export default About;