import { Carousel } from "@/components/Carousel";
import { Footer } from "@/components/Footer";
import { Hero } from "@/components/Hero";
import { Navbar, MobileNavbar } from "@/components/Navbar";
import Link from "next/link";

export default function Home() {
  return (
    <main className="h-screen">
      <div className="block md:hidden">
        <MobileNavbar/>
      </div>
      <div className="hidden md:block">
        <Navbar/>
      </div>
      <Hero/>
      <h2 className="text-xl md:text-3xl text-center py-3 font-semibold px-2">Comprehensive property data from multiple trusted sources, available for most major cities — conveniently in one place.</h2>
      <Carousel/>
      <div className="flex justify-center py-6">
        <Link className="btn w-42 text-base md:btn-lg bg-primary text-white hover:bg-[#457F9F]" href="/sign-up">Pre-Register Now!</Link>
      </div>
      <footer className="flex justify-center text-center border-t-2">
        <Footer/>
      </footer>
    </main>
  );
}
