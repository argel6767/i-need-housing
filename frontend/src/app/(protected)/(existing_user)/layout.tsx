import {ExistingUserProvider} from "@/app/(protected)/(existing_user)/ExistingUserContext";


export default function ExistingUserLayout({children,}: {
    children: React.ReactNode;
}) {
    return (
        <ExistingUserProvider>
            {children}
        </ExistingUserProvider>
    );
}