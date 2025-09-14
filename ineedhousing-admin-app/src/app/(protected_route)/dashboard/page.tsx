import {Navbar} from "@/components/navbar";
import {WelcomeMessage} from "@/app/(protected_route)/dashboard/components";
import Link from "next/link";
import {LogStreamViewer} from "@/components/service-functions";


export default function Dashboard() {
    return (
        <main className={"h-screen flex flex-col items-center gap-8"}>
            <Navbar />
            <WelcomeMessage />
            <section className={"flex flex-col items-center gap-8"}>
                <h2 className={"text-3xl"}>Check a Service</h2>
                <ul className={"flex flex-col items-center gap-8 text-xl text-white"}>
                    <li className={"hover:bg-primary-light bg-primary btn-lg p-4 rounded-lg shadow-lg"}><Link
                        href={"/dashboard/ineedhousing_api"}>INeedHousing API</Link>
                    </li>
                    <li className={"hover:bg-primary-light bg-primary btn-lg p-4 rounded-lg shadow-lg"}><Link
                        href={"/dashboard/cron_job_service"}>Cron Job
                        Service</Link></li>
                    <li className={"hover:bg-primary-light bg-primary btn-lg p-4 rounded-lg shadow-lg"}><Link
                        href={"/dashboard/email_service"}>Email
                        Service</Link></li>
                    <li className={"hover:bg-primary-light bg-primary btn-lg p-4 rounded-lg shadow-lg"}><Link
                        href={"/dashboard/keymaster_service"}>Keymaster
                        Service</Link></li>
                    <li className={"hover:bg-primary-light bg-primary btn-lg p-4 rounded-lg shadow-lg"}><Link
                        href={"/dashboard/new_listings_service"}>New Listings
                        Service</Link></li>
                </ul>
            </section>
        </main>
    )
}