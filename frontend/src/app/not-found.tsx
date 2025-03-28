"use client"

import { useEffect, useState } from "react"
import { sleep } from "./utils/utils";
import { useRouter } from "next/navigation";

export default function NotFound() {

    const [second, setSecond] = useState<number>(5);
    const router = useRouter();


    useEffect(() => {
        const countdown = async() => {
            for (let i = 5; i > 0; i--) {
                await sleep(1000);
                setSecond((prev) => prev -1);
            }
            router.replace("/");
        }
        countdown();
    }, [router])

    return (
        <main className="flex flex-col justify-center items-center h-screen bg-slate-100">
            <span className="bg-slate-300 text-3xl md:text-4xl text-center rounded-lg shadow-lg p-4 space-y-4">
                <h1 >404 NOT FOUND</h1>
                <h1>The page you are trying to reach does not exist!</h1>
            </span>
            <h2 className="mt-5 underline underline-offset-4 text-2xl md:text-3xl">Redirecting you back to our landing page in {second}</h2>
        </main>
    )
}

