"use client"
import Image from "next/image"
import icon from "../../public/file.svg"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { logout } from "@/endpoints/auths"
import { useState } from "react"
import { sleep } from "@/app/utils/utils"
import { Loader } from "lucide-react"

export const Navbar = () => {
    return (
       <div className="navbar bg-background border-b-2 border-gray-200 shadow-md" data-testid="navbar">
  <div className="navbar-start">
    <Link data-testid="INeedHousing" href={"/"} className="btn btn-ghost text-3xl text-primary font-bold">INeedHousing</Link>
  </div>
  <div className="navbar-center  lg:flex">
    <ul className="menu menu-horizontal px-1 items-center text-lg ">
      <li className="hover:underline underline-offset-4"><Link href={"/about"}>About</Link></li>
      <li><Image src={icon} alt="Icon" width={90} height={75}/></li>
      <li><Link className="hover:underline underline-offset-4" href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link></li>
    </ul>
  </div>
  <div className="navbar-end">
    <Link className="btn btn-primary hover:bg-[#457F9F]" href="/sign-in">Sign In</Link>
  </div>
</div> 
    )
}

export const LoggedInNavBar = () => {
  const [isError, setIsError] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false)
  const router = useRouter();
  const logoutUser = async () => {
    setIsLoading(true);
    const response = await logout();
    console.log("response: " + response);
    if (response === "Logged out successfully") {
      router.push("/");
    }
    else {
      setIsError(true);
      await sleep(1700);
      setIsError(false);
    }
  }

  return (
    <div className="navbar bg-base-100 py-4">
  <div className="flex-1">
    <Link href={"/home"} className="btn btn-ghost text-3xl sm:text-5xl text-primary font-bold">INeedHousing<Image src={"./file.svg"} width={50} height={50} alt="Logo"/></Link>
  </div>
  <div className="flex-none gap-2">
    <div className="dropdown dropdown-end">
      <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar mr-3">
        <div className="w-12 rounded-full shadow-2xl">
          <img
            alt="Tailwind CSS Navbar component"
            src="https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg" />
        </div>
      </div>
      <ul
        tabIndex={0}
        className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[99] mt-3 w-52 p-2 shadow ">
        <li>
        </li>
        <li><Link href={"/settings"}>Settings</Link></li>
        <li><a onClick={logoutUser}>Logout <Loader size={22} className={`ml-2 animate-pulse ${isLoading ? "" : "hidden"}`}/></a></li>
        {isError && <li className="text-red-500">Could not log out user! Try again</li>}
      </ul>
    </div>
  </div>
</div>
  )
}