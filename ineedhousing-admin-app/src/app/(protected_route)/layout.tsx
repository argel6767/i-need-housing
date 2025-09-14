import {ProtectedRoute} from "@/app/(protected_route)/protected-route";
import {QueryClientLayoutProvider} from "@/app/(protected_route)/dashboard/components";

export default function ProtectedLayout({children,}: {
    children: React.ReactNode;
}) {
    return (
        <QueryClientLayoutProvider>
            <ProtectedRoute>
                {children}
            </ProtectedRoute>
        </QueryClientLayoutProvider>
        );
}