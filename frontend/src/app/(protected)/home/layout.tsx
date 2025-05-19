import {HomeProvider} from "@/app/(protected)/home/HomeContext";

export default function HomeLayout({children,}: {
    children: React.ReactNode;
}) {
    return (
        <HomeProvider>
            {children}
         </HomeProvider>
    );
}