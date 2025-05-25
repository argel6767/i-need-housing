import {HomeProvider} from "@/app/(protected)/(existing_user)/home/HomeContext";
import {Metadata} from "next";

export const metadata: Metadata = {
    title: 'Home | INeedHousing',
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