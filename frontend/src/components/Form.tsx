"use client"
import { isValidEmail, sleep } from "@/app/utils/utils";
import { AuthenticateUserDto } from "@/interfaces/requests/authsRequests";
import { useRouter } from "next/navigation";
import { FormEvent, useRef, useState } from "react";
import { Loading } from "./Loading";
import { ResendVerificationEmail } from "./ResendEmailVerification";

interface FormProps {
    buttonLabel:string
    loadingMessage?:string
    route:string
    request: (credentials:AuthenticateUserDto) => Promise<any>
}

/**
 * Reusable Form component used for sign-in, sign-up, and Verify
 * @param param
 * @returns 
 */
export const Form = ({buttonLabel, loadingMessage, route, request}: FormProps) => {

    const router = useRouter();
    const [isCallFailed, setIsCallFailed] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isRequestingEmail, setIsRequestingEmail] = useState<boolean>(false);
    const credentials: AuthenticateUserDto = {
        username:"",
        password:""
    }   
    

    const handleRegistration = async(e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true)
        const formData = new FormData(e.currentTarget);
        credentials.username = formData.get("email") as string;
        credentials.password = formData.get("password") as string;
        sessionStorage.setItem("email", credentials.username);
        const data = await request(credentials);
        if(data === "logged in" || data === "user created") { //successful
            sessionStorage.setItem("email", credentials.username);
            router.push(route)
        }
        else if (data === "user is not verified") {
            setIsRequestingEmail(true);
        }
        else if (data === "user could not be created" || data === "login failed") {
            setIsCallFailed(true);
            await sleep(1700);
            setIsCallFailed(false);
        }
    }

    const handleResendHref = (e:any) => {
        e.preventDefault();
        router.push("/sign-up/verify");
    }

    //rendered with failed called
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
    <form className="mt-5" onSubmit={handleRegistration}>
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
                />
            </div>
            <div className={`font-semibold ${isRequestingEmail ? "display" : "hidden"}`  } onClick={handleResendHref}>
                <ResendVerificationEmail email={credentials.username} message={"Your account is not verified."} button={"Request code."}/>
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
                />
            </div>
            </div>
            <div>
            <button
                className="inline-flex w-full items-center justify-center rounded-md bg-primary hover:bg-[#457F9F] px-3.5 py-2.5 font-semibold leading-7 text-white"
                type="submit">
                {buttonLabel}
            </button>
            </div>
        </div>
    </form>
    )
}