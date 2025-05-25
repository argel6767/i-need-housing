
import {ChangeProfilePicture} from "@/app/(protected)/(existing_user)/settings/ChangeProfilePicture";
import Link from "next/link";
import Image from "next/image";
import React from "react";
import {Metadata} from "next";

export const metadata: Metadata = {
    title: 'Settings | INeedHousing',
    description: "INeedHousing User Settings",
};

const Settings = () => {
    return (
        <div className={"flex flex-col items-center justify-center"}>
            <main className={"flex flex-col items-center justify-center gap-8 p-10 w-full md:w-3/4 shadow-2xl rounded-lg"}>
                <nav>
                    <Link href={"/home"}
                          className="btn btn-ghost text-3xl md:text-4xl lg:text-6xl text-primary font-bold  hover:scale-110 transition-transform duration-300">INeedHousing<Image
                        src={"./file.svg"} width={50} height={50} alt="Logo"/></Link>
                </nav>
                <ChangeProfilePicture/>
            </main>
        </div>
    )
}

export default Settings;