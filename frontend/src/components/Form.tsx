"use client"
import { sleep } from "@/app/utils/utils";
import { AuthenticateUserDto } from "@/interfaces/requests/authsRequests";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Loading } from "./Loading";

interface FormProps {
    buttonLabel:string
    loadingMessage?:string
    route:string
    request: (credentials:AuthenticateUserDto) => Promise<any>
}

export const Form = ({buttonLabel, loadingMessage, route, request}: FormProps) => {

    const router = useRouter();
    const [credentials, setCredentials] = useState<AuthenticateUserDto>({
        username:"",
        password:"",
    });
    const [isCallFailed, setIsCallFailed] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isAccountVerified, setIsAccountVerified] = useState<boolean>(true);

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    const isValidEmail = (email: string): boolean => {
        return emailRegex.test(email);
    }
        
    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCredentials({...credentials, username: event.target.value});
        if (!isValidEmail(credentials.username)) {
            console.log("invalid email");
        }
    }
    
    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCredentials({...credentials, password: event.target.value})
    }

    const handleRegistration = async() => {
        setIsLoading(true)
        const data = await request(credentials);
        setIsLoading(false);
        if(data) {
            router.push(route)
        }
        else {
            setIsCallFailed(true);
            await sleep(1700);
            setIsCallFailed(false);
        }
    }

    if (isCallFailed) {
        return (
            <section className="flex flex-col gap-2">
                <h1 className="flex-1 text-xl text-red-600 font-bold">Something went wrong! Please try again. {/*TODO Implement this*/}</h1>
                <p className="text-lg">The following may be at fault:</p>
                <span className="px-2">
                    <ul className="list-disc">
                        <div className="space-y-1 italic">
                            <li>The Backend server is currently down.</li>
                            <li>The email you submitted is already taken.</li>
                            <li>The email or password is invalid.</li>
                        </div>
                        
                    </ul>
                </span>
            </section>
        )
    }

    if (isLoading) {
        return <Loading loadingMessage={loadingMessage}/>
    }
        
    return (
    <form className="mt-5">
        <div className="space-y-6">
            <div>
            <label className="text-base font-medium text-gray-900">
                Email Address
            </label>
            <div className="mt-2">
                <input
                placeholder="Email"
                type="email"
                className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                name="email"
                value={credentials.username}
                onChange={handleUsernameChange}
                />
            </div>
            </div>
            <div>
            <div className="flex items-center justify-between">
                <label className="text-base font-medium text-gray-900">
                Password
                </label>
            </div>
            <div className="mt-2">
                <input
                placeholder="Password"
                type="password"
                className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                name="password"
                value={credentials.password}
                onChange={handlePasswordChange}
                />
            </div>
            </div>
            <div>
            <button
                className="inline-flex w-full items-center justify-center rounded-md bg-primary hover:bg-[#457F9F] px-3.5 py-2.5 font-semibold leading-7 text-white"
                type="button"
                onClick={handleRegistration}
            >
                {buttonLabel}
            </button>
            </div>
        </div>
    </form>
    )
}