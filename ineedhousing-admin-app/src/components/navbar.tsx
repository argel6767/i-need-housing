import Link from "next/link";
import {Logout} from "@/components/logout";

export const Navbar = () => {
    return (
        <div className="navbar  shadow-lg rounded">
            <div className="navbar-start px-2">
                <div className="dropdown">
                    <div tabIndex={0} role="button" className="btn btn-ghost btn-circle">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24"
                             stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                  d="M4 6h16M4 12h16M4 18h7"/>
                        </svg>
                    </div>
                    <ul
                        tabIndex={0}
                        className="menu menu-sm dropdown-content bg-slate-200 rounded-box z-1 mt-3 w-52 p-2 shadow gap-4">
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard"}>Home</Link></li>
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard/ineedhousing_api"}>INeedHousing API</Link></li>
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard/cron_job_service"}>Cron Job
                            Service</Link></li>
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard/email_service"}>Email
                            Service</Link></li>
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard/keymaster_service"}>Keymaster
                            Service</Link></li>
                        <li className={"hover:bg-slate-100"}><Link href={"/dashboard/new_listings_service"}>New Listings
                            Service</Link></li>
                        <li className={"hover:bg-slate-100"}><Logout/></li>
                    </ul>
                </div>
            </div>
            <div className="navbar-center">
                <h1 className="text-primary text-4xl font-semibold">INeedHousing</h1>
            </div>
            <div className="navbar-end">

            </div>
        </div>
    )
}