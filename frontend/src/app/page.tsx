import { Carousel } from "@/components/Carousel";
import { Footer } from "@/components/Footer";
import { Hero } from "@/components/Hero";
import { Navbar } from "@/components/Navbar";
import Link from "next/link";

export default function Home() {
  return (
    <main className="h-screen">
      <Navbar/>
      <Hero/>
      <h2 className="text-3xl text-center py-3 font-semibold">Comprehensive property data from multiple trusted sources, available for most major cities — conveniently in one place.</h2>
      <Carousel/>
      <div className="flex justify-center py-6">
        <Link className="btn btn-lg bg-primary text-white hover:bg-[#457F9F]" href="/sign-up">Pre-Register Now!</Link>
      </div>
      <footer className="flex justify-center text-center border-t-2">
        <Footer/>
      </footer>
    </main>
  );
}
