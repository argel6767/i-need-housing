
import { redirect } from 'next/navigation';

export default async function NotFound() {
    await new Promise(resolve => setTimeout(resolve, 3000)); // 3 second delay
    redirect('/');

    return (
        <main className="flex flex-col justify-center items-center h-screen">
            <h1>404 NOT FOUND</h1>
            <h1>The page you are trying to reach does not exist!</h1>
        </main>
    );
}