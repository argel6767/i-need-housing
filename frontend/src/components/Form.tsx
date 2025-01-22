"use client"
import { sleep } from "@/app/utils/utils";
import { register } from "@/endpoints/auths";
import { AuthenticateUserDto } from "@/interfaces/requests/authsRequests";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface FormProps {
    buttonLabel:string
}

export const Form = ({buttonLabel}: FormProps) => {

    const router = useRouter();
    const [credentials, setCredentials] = useState<AuthenticateUserDto>({
        username:"",
        password:"",
    });
    const [isCallFailed, setIsCallFailed] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);

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
        const data = await register(credentials);
        setIsLoading(false);
        if(data) {
            router.push("/verify")
        }
        else {
            setIsCallFailed(true);
            await sleep(2000);
            setIsCallFailed(false);
        }
    }

    if (isCallFailed) {
        return <h1>Call failed oopsie! {/*TODO Implement this*/}</h1>
    }

    if (isLoading) {
        return <h1>Loading...</h1>
    }
        
    return (
    <form className="mt-5">
        <div className="space-y-4">
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