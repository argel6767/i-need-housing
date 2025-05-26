"use client"
import {isValidEmail, isValidPassword, sleep} from "@/utils/utils";
import { AuthenticateUserDto } from "@/interfaces/requests/authsRequests";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import {Loading, LoadingBars} from "./Loading";
import { ResendVerificationEmail } from "./ResendEmailVerification";
import {useGlobalContext} from "@/components/GlobalContext";
import {post} from "axios";

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
    const {setIsFirstTimeUser} = useGlobalContext();
    const credentials: AuthenticateUserDto = {
        username:"",
        password:""
    }
    
   //TODO Possibly replace this with a function prop that a parent component can to eliminate all the if statements
    const handleRegistration = async(e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true)
        const formData = new FormData(e.currentTarget);
        credentials.username = formData.get("email") as string;
        credentials.password = formData.get("password") as string;
        sessionStorage.setItem("email", credentials.username);
        const data = await request(credentials);
        if (data === "new user") {
            setIsFirstTimeUser(true);
            router.push("/new-user/user-type");
        }
        else if(data === "logged in" || data === "user created") { //successful
            setIsFirstTimeUser(false);
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
            setIsLoading(false);
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

interface NewFormProps {
    buttonLabel:string
    request: (credentials:AuthenticateUserDto) => Promise<any>
    errorMessage: string
}

export const NewForm = ({buttonLabel, request, errorMessage}:NewFormProps) => {
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isInvalidCredentials, setIsInvalidCredentials] = useState({username:false, password:false})
    const credentials: AuthenticateUserDto = {
        username:"",
        password:""
    }

    const handleUsernameValidity = (newState:boolean) => {
        setIsInvalidCredentials(prevState => ({
           ...prevState, username: newState
        }))
    }

    const handlePasswordValidity = (newState:boolean) => {
        setIsInvalidCredentials(prevState => ({
            ...prevState, password: newState
        }))
    }


    const runAuthFunction = async(e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        e.stopPropagation();
        const formData = new FormData(e.currentTarget);
        credentials.username = formData.get("email") as string;
        credentials.password = formData.get("password") as string;
        if (!isValidEmail(credentials.username)) {
            handleUsernameValidity(true);
            await sleep(1500);
            handleUsernameValidity(false);
        }
        else if (!isValidPassword(credentials.password)) {
            handlePasswordValidity(true);
            await sleep(1500);
            handlePasswordValidity(false);
        }
        else {
            setIsLoading(true);
            await request(credentials);
            setIsLoading(false);
        }
    }

    if (isLoading) {
        return (
            <div className={"flex flex-col justify-center items-center gap-4 min-w-96"}>
                <LoadingBars/>
                <h2 className={"text-xl"}>Authenticating Request</h2>
            </div>
        )
    }


    return (
        <form className={"py-2 min-w-96  flex flex-col justify-items-start gap-8"} onSubmit={runAuthFunction}>
            <span>
                <label className="text-sm text-gray-900">
                    Email
                    <input placeholder="Email" type="email"
                        className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                        name="email"/>
                    <div className="label">
                        {isInvalidCredentials.username && <span className="label-text-alt text-red-500 animate-fade">Email is not valid. Try again.</span>}
                    </div>
                </label>
            </span>
                <span>
                    <label className="text-sm text-gray-900">
                        Password
                    <input placeholder="Password" type="password"
                        className=" h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50 "
                        name="password"/>
                        <div className="label">
                        {isInvalidCredentials.password && <span className="label-text-alt text-red-500 animate-fade">Password is in not valid. A password must have the following:
                            <ul className={"pt-1"}>
                                <li>One lower case letter (a-z)</li>
                                <li>One upper case letter (A-Z)</li>
                                <li>A digit (0-9)</li>
                                <li>One special symbol (@#$%^&+=!*?)</li>
                                <li>No spaces</li>
                            </ul>
                        </span>}
                        </div>
                    </label>
                </span>
            <button className="inline-flex w-full items-center justify-center rounded-md bg-primary hover:bg-[#457F9F] px-3.5 py-2.5 font-semibold leading-7 text-white" type="submit">
                {buttonLabel}
            </button>
            <p className={"text-red-500 text-center"}>{errorMessage}</p>
        </form>
    )
}
