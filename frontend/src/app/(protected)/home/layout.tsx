import {HomeProvider} from "@/app/(protected)/home/HomeContext";
import {Metadata} from "next";

export const metadata: Metadata = {
    title: 'INeedHousing | Home',
    description: "INeedHousing Listings Dashboard"
};


export default function HomeLayout({children,}: {
    children: React.ReactNode;
}) {
    return (
        <HomeProvider>
            {children}
         </HomeProvider>
    );
}