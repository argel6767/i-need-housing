"use client"
import Image from "next/image"
import icon from "../../public/file.svg"
import Link from "next/link"
import { useRouter } from "next/navigation"


/**
 * Navbar for desktop/laptop
 * @returns 
 */
export const Navbar = () => {
    return (
       <div className="navbar bg-background border-b-2 border-gray-200 shadow-md" data-testid="navbar">
  <div className="navbar-start">
    <Link data-testid="INeedHousing" href={"/"} className="btn btn-ghost text-3xl text-primary font-bold">INeedHousing</Link>
  </div>
  <div className="navbar-center  lg:flex">
    <ul className="menu menu-horizontal px-1 items-center text-lg ">
      <li className="hover:underline"><Link href={"/about"}>About</Link></li>
      <li><Image src={icon} alt="Icon" width={90} height={75}/></li>
      <li><Link className="hover:underline" href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link></li>
    </ul>
  </div>
  <div className="navbar-end">
    <Link className="btn btn-primary hover:bg-[#457F9F] hidden" href="/sign-up">Register</Link>
  </div>
</div> 
    )
}

/**
 * Navbar that renders when the user is on a phone
 * @returns
 */
export const MobileNavbar = () => {
  return (
    <div className="navbar bg-base-100">
      <div className="navbar-start">
        <div className="dropdown">
          <div tabIndex={0} role="button" className="btn btn-ghost btn-circle">
            <svg
          xmlns="http://www.w3.org/2000/svg" className="h-5 w-5"fill="none"viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h7" />
            </svg>
        </div>
        <ul tabIndex={0} className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
          <li><Link href={"/about"}>About</Link></li>
          <li><Link href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link></li>
        </ul>
      </div>
    </div>
  <div className="navbar-center">
    <Link data-testid="INeedHousing" href={"/"} className="btn btn-ghost text-2xl text-primary font-bold">INeedHousing<Image src={icon} alt="Icon" width={40} height={40}/></Link>
  </div>
  <div className="navbar-end">
  </div>
  </div>
  )
}

/**
 * When user is logged in
 * @returns 
 */
export const LoggedInNavBar = () => {
  const router = useRouter();
  const logoutUser = () => {
    sessionStorage.clear();
    router.push("/");
  }

  return (
    <div className="navbar bg-base-100 py-4">
  <div className="flex-1">
    <Link href={"/home"} className="btn btn-ghost text-3xl sm:text-5xl text-primary font-bold">INeedHousing</Link>
  </div>
  <div className="flex-none gap-2">
    <div className="dropdown dropdown-end">
      <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar mr-3">
        <div className="w-10 rounded-full">
          <img
            alt="Tailwind CSS Navbar component"
            src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp" />
        </div>
      </div>
      <ul
        tabIndex={0}
        className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[99] mt-3 w-52 p-2 shadow ">
        <li>
        </li>
        <li><Link href={"/settings"}>Settings</Link></li>
        <li><a onClick={logoutUser}>Logout</a></li>
      </ul>
    </div>
  </div>
</div>
  )
}