"use client"
import Image from "next/image"
import icon from "../../public/file.svg"
import React, {ReactNode, useState} from "react"
import { Loader } from "lucide-react"
import {MaxPrice, OtherFilters, RangeBar} from "@/app/(protected)/(existing_user)/home/InnerFilters";
import {useGlobalContext} from "@/components/GlobalContext";
import {useHomeContext} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {updateUserPreferencesViaFilters} from "@/endpoints/preferences";
import {filterListingsByPreferences} from "@/endpoints/listings";
import {User} from "@/components/User";
import Link from "next/link";


interface MobileListItemsProps {
    children: React.ReactNode;
    isModalUp?: boolean;
}

/**
 * List of items in nav pop down for mobile
 * @param children
 * @param isModalUp
 * @constructor
 */
const MobileListItems = ({ children, isModalUp=false}: MobileListItemsProps) => {
    const [isOpen, setIsOpen] = useState(false);

    // Toggle dropdown manually
    const toggleDropdown = () => setIsOpen(!isOpen);

    return (
        <nav className="navbar-start">
            {/* Add a custom class to keep the dropdown open */}
            <div className={`dropdown ${isOpen ? 'dropdown-open' : ''} ${isModalUp ? 'dropdown-modal-open' : ''}`}>
                <div
                    role="button"
                    className="btn btn-ghost btn-circle"
                    onClick={toggleDropdown}
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-5 w-5"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                    >
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h7"/>
                    </svg>
                </div>
                <ul className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                    {React.Children.map(children, (child, i) => (
                        <li
                            className="hover:underline underline-offset-4 hover:scale-105 transition-transform duration-300 hover:bg-gray-100 rounded-2xl"
                            key={i}
                        >
                            {child}
                        </li>
                    ))}
                </ul>
            </div>
        </nav>
    );
};

/**
 * Navbar for desktop/laptop
 * @returns
 */
export const Navbar = () => {
    const isSignedIn = () => {
        if (sessionStorage.getItem("status") === null) {
            sessionStorage.setItem("status", "signed out")
        }
        return sessionStorage.getItem("status") === "signed in";
    }

  return (
      <main className="navbar bg-background border-b-2 border-gray-200 shadow-md" data-testid="navbar">
        <div className="navbar-start">
          <Link data-testid="INeedHousing" href={"/"}
                className="btn btn-ghost text-4xl lg:text-5xl text-primary hover:scale-110 transition-transform duration-300">INeedHousing<Image src={"./file.svg"} width={50}
                                                                                    height={50} alt="Logo"/></Link>
        </div>
        <div className="navbar-center lg:flex">
          <ul className="menu menu-horizontal px-1 items-center text-lg ">
            <li className="hover:underline underline-offset-4"><Link href={"/about"}>About</Link></li>
            <li><Image src={icon} alt="Icon" width={90} height={75}/></li>
            <li><Link className="hover:underline underline-offset-4" href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link></li>
          </ul>
        </div>
        <div className="navbar-end">
            {!isSignedIn() && <Link className="btn btn-primary hover:bg-[#457F9F] w-32" href="/sign-in">Sign In</Link>}
        </div>
      </main>
  )
}

/**
 * Navbar that renders when the user is on a phone
 * @returns
 */
export const MobileNavbar = () => {
  return (
    <main className="navbar bg-base-100">
      <MobileListItems>
        <Link href="/home">View Listings</Link>
        <Link href={"/about"}>About</Link>
        <Link href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link>
      </MobileListItems>
      <div className="navbar-center">
        <Link data-testid="INeedHousing" href={"/"} className="btn btn-ghost text-3xl sm:text-4xl md:text-5xl text-primary font-bold">INeedHousing<Image src={icon} alt="Icon" width={40} height={40}/></Link>
      </div>
      <div className="navbar-end">
      </div>
    </main>
  )
}


/**
 * When user is logged in
 * @returns 
 */
export const LoggedInNavBar = () => {
  return (
      <div className="navbar bg-base-100 py-4">
      <div className="flex-1">
        <Link href={"/"} className="btn btn-ghost text-3xl sm:text-5xl text-primary font-bold  hover:scale-110 transition-transform duration-300">INeedHousing<Image src={"./file.svg"} width={50} height={50} alt="Logo"/></Link>
      </div>
          <User/>
      </div>
  )
}

interface LoggedInMobileNavbarProps {
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>;
    refetch: any,
}

/**
 * When user is on a phone and logged in
 * handles the home menu navigation as well
 * @returns
 */
export const LoggedInMobileNavbar = ({setIsModalUp, refetch}:LoggedInMobileNavbarProps) => {

    const {userPreferences, setUserPreferences} = useGlobalContext();
    const {setFilterRendered, isListingsFiltered, setIsListingsFiltered, isFiltersChanged, setIsFiltersChanged, listings, setListings, setInitialPreferences,
        isFilterModalUp, isFiltering, setIsFiltering, isSaving, setIsSaving, isResetting, setIsResetting} = useHomeContext();

    const handleFilterRendered = (filter: ReactNode) => {
        setFilterRendered(filter);
        setIsModalUp(true);
    }

    //saves the updated preferences for user for later use
    const saveUserPreferences = async () => {
        setIsSaving(true);
        const response = await updateUserPreferencesViaFilters(userPreferences!);
        setUserPreferences(response);
        setInitialPreferences(response);
        setIsFiltersChanged(false);
        setIsSaving(false);
    }

    //calls the filtering endpoint  using the id of the user's preferences and listings

    const handleFiltering = async () => {
        if (!userPreferences) {
            console.warn("No user preferences available");
            return; // Early return if no preferences
        }

        setIsFiltering(true);
        const data = await filterListingsByPreferences({listings: listings, id: userPreferences.id});
        console.log(data);
        setListings(data);
        setIsFiltering(false);
        setIsListingsFiltered(true);
    }

    //refetches the listings to reset from filtered state
    const handleRefetch = async () => {
        setIsResetting(true);
        const response = await refetch();
        setListings(response.data);
        setIsListingsFiltered(false);
        setIsResetting(false);
    }

  return (
      <main className="navbar bg-base-100">
          <MobileListItems isModalUp={isFilterModalUp}> {/*Each button determines what filter will be rendered when clicked*/}
              <button onClick={() => handleFilterRendered(<RangeBar initialRange={userPreferences?.maxRadius}
                                                                    setUpdatedPreferences={setUserPreferences}/>)}>Change
                  Distance
              </button>
              <button onClick={() => handleFilterRendered(<MaxPrice maxPrice={userPreferences?.maxRent}
                                                                    setUpdatedPreferences={setUserPreferences}/>)}>Price
              </button>
              <button onClick={() => handleFilterRendered(<OtherFilters setUpdatedPreferences={setUserPreferences}
                                                                        updatedPreferences={userPreferences!}/>)}>Other
              </button>
              <button onClick={handleFiltering}>Filter Results <Loader size={22}
                                                                       className={`animate-pulse ${isFiltering ? "" : "hidden"}`}/>
              </button>
              <button className={`${!isFiltersChanged && "hidden"}`} onClick={saveUserPreferences}>Save <Loader
                  size={22} className={`animate-pulse ${isSaving ? "" : "hidden"}`}/></button>
              <button className={`${!isListingsFiltered && "hidden"}`} onClick={handleRefetch}>Reset Listings <Loader
                  size={22} className={`animate-pulse ${isResetting ? "" : "hidden"}`}/></button>
          </MobileListItems>
          <div className="navbar-center">
              <Link data-testid="INeedHousing" href={"/"} className="hover:scale-110 transition-transform duration-300"><Image
                  src={icon} alt="Icon" width={40} height={40}/></Link>
        </div>
        <div className="navbar-end">
          <User/>
        </div>
      </main>
  )
}