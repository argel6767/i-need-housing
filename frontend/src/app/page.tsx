import { Carousel } from "@/components/Carousel";
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
      <div className="flex justify-center py-5">
        <Link className="btn btn-lg bg-primary text-white hover:bg-[#457F9F]" href="/auth">View Listings</Link>
      </div>
      
    </main>
  );
}
