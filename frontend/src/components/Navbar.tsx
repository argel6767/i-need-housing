import Image from "next/image"
import icon from "../../public/file.svg"
import Link from "next/link"

export const Navbar = () => {
    return (
       <div className="navbar bg-background border-b-2 border-gray-200 shadow-md" data-testid="navbar">
  <div className="navbar-start">
    <Link data-testid="INeedHousing" href={"/"} className="btn btn-ghost text-xl text-primary font-bold">INeedHousing</Link>
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