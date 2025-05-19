// app/(protected)/layout.tsx
import { ProtectedRoute } from '@/app/(protected)/ProtectedRoute';

export default function ProtectedLayout({children,}: {
    children: React.ReactNode;
}) {
    return <ProtectedRoute>{children}</ProtectedRoute>;
}