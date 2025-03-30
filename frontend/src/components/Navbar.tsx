"use client"
import Image from "next/image"
import icon from "../../public/file.svg"
import Link from "next/link"
import { useRouter } from "next/navigation"

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
    <Link className="btn btn-primary hover:bg-[#457F9F]" href="/sign-in">Sign In</Link>
  </div>
</div> 
    )
}

export const LoggedInNavBar = () => {
  const router = useRouter();
  const logoutUser = () => {
    sessionStorage.clear();
    router.push("/");
  }

  return (
    <div className="navbar bg-base-100 py-4">
  <div className="flex-1">
    <Link href={"/home"} className="btn btn-ghost text-3xl sm:text-5xl text-primary font-bold">INeedHousing<Image src={"./file.svg"} width={50} height={50} alt="Logo"/></Link>
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