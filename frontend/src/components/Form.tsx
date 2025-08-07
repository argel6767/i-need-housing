"use client"
import {isValidEmail, isValidPassword, sleep} from "@/utils/utils";
import { AuthenticateUserDto } from "@/interfaces/requests/authsRequests";
import { FormEvent, useState } from "react";
import { LoadingBars} from "./Loading";

interface NewFormProps {
    buttonLabel:string
    request: (credentials:AuthenticateUserDto) => Promise<any>
    isLoading: boolean
}

export const Form = ({buttonLabel, request, isLoading}:NewFormProps) => {
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
            await request(credentials);
        }
    }

    if (isLoading) {
        return (
            <div className={"flex flex-col justify-center items-center gap-4 min-w-60 md:min-w-96"}>
                <LoadingBars/>
                <h2 className={"text-xl"}>Authenticating Request</h2>
            </div>
        )
    }


    return (
        <form className={"py-2 min-w-64 md:min-w-96  flex flex-col justify-items-start gap-8"} onSubmit={runAuthFunction}>
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
        </form>
    )
}
