import { Footer } from "@/components/Footer";
import { Navbar, MobileNavbar } from "@/components/Navbar";
import Link from "next/link";

export default function Home() {
  return (
    <main className="min-h-screen flex flex-col">
      <nav className="block lg:hidden">
        <MobileNavbar />
      </nav>
      <nav className="hidden lg:block">
        <Navbar />
      </nav>

      <section className="flex-1 flex items-center justify-center bg-base-200 px-4 py-16">
        <div className="max-w-2xl w-full text-center flex flex-col items-center gap-6 animate-fade">
          <div className="badge badge-error badge-lg text-white font-semibold tracking-widest uppercase px-4 py-4 text-sm">
            Project Discontinued
          </div>

          <h1 className="text-4xl md:text-5xl font-bold text-foreground leading-tight">
            INeedHousing Is No Longer Active
          </h1>

          <div className="divider w-24 mx-auto border-secondary" />

          <p className="text-lg md:text-xl text-gray-600 leading-relaxed">
            Thank you to everyone who used and supported this project. As of
            now,{" "}
            <span className="font-semibold text-primary">INeedHousing</span> has
            been officially discontinued and is no longer being actively
            maintained or updated.
          </p>

          <p className="text-base md:text-lg text-gray-500 leading-relaxed">
            The listings, search features, and user accounts are no longer
            available. The source code remains publicly accessible on GitHub if
            you'd like to explore, fork, or build upon it.
          </p>

          <div className="alert alert-warning max-w-lg shadow-md">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6 shrink-0 stroke-current"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
              />
            </svg>
            <span className="text-sm font-medium">
              All services have been shut down. Any existing accounts and data
              are no longer accessible.
            </span>
          </div>

          <div className="flex flex-col sm:flex-row gap-4 pt-2">
            <Link
              href="https://github.com/argel6767/i-need-housing"
              className="btn bg-primary text-white hover:bg-[#457F9F] btn-lg"
              target="_blank"
              rel="noopener noreferrer"
            >
              View Source Code
            </Link>
            <Link
              href="/about"
              className="btn btn-outline btn-lg border-primary text-primary hover:bg-primary hover:text-white"
            >
              About the Project
            </Link>
          </div>
        </div>
      </section>

      <footer className="flex justify-center text-center border-t-2">
        <Footer />
      </footer>
    </main>
  );
}
