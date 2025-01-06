import Image from "next/image"
import icon from "../../public/file.png"

export const Navbar = () => {
    return (
       <div className="navbar bg-background border-b-2 border-gray-200 shadow-md">
  <div className="navbar-start">
    <a className="btn btn-ghost text-xl text-primary font-bold" href="/">INeedHousing</a>
  </div>
  <div className="navbar-center hidden lg:flex">
    <ul className="menu menu-horizontal px-1 items-center text-lg">
      <li><a>Item 1</a></li>
      <li><Image src={icon} alt="Icon" width={75} /></li>
      <li><a>Item 3</a></li>
    </ul>
  </div>
  <div className="navbar-end">
    <a className="btn btn-primary" href="/auth">Sign In</a>
  </div>
</div> 
    )
}