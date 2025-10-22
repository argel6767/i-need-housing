
import {ChangeProfilePicture} from "@/app/(protected)/(existing_user)/settings/ChangeProfilePicture";
import React from "react";
import {Metadata} from "next";
import {BackButton} from "@/components/back";
import {Footer} from "@/components/Footer";
import {UserDetails} from "@/app/(protected)/(existing_user)/settings/components";

export const metadata: Metadata = {
    title: 'Settings | INeedHousing',
    description: "INeedHousing User Settings",
};

const Settings = () => {
    return (
        <main className={"flex flex-col items-center justify-center"}>
            <section
                className={"flex flex-col items-center justify-center gap-8 p-10 w-full md:w-3/4 shadow-2xl rounded-lg"}>
                <nav className={"flex justify-start w-full"}>
                    <BackButton backPath={"/home"}/>
                </nav>
                <ChangeProfilePicture/>
                <span className={"w-full py-4"}>
                    <UserDetails/>
                </span>
            </section>
            <footer className="w-full border-t-2 mt-2">
                <Footer/>
            </footer>
        </main>
    )
}

export default Settings;