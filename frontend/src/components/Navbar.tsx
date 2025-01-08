import Image from "next/image"
import icon from "../../public/file.png"
import Link from "next/link"

export const Navbar = () => {
    return (
       <div className="navbar bg-background border-b-2 border-gray-200 shadow-md">
  <div className="navbar-start">
    <Link href={"/"} className="btn btn-ghost text-xl text-primary font-bold">INeedHousing</Link>
  </div>
  <div className="navbar-center  lg:flex">
    <ul className="menu menu-horizontal px-1 items-center text-lg ">
      <li><Link href={"/about"}>About</Link></li>
      <li><Image src={icon} alt="Icon" width={75} /></li>
      <li><Link href={"https://github.com/argel6767/i-need-housing"}>Source Code</Link></li>
    </ul>
  </div>
  <div className="navbar-end">
    <a className="btn btn-primary" href="/auth">Sign In</a>
  </div>
</div> 
    )
}