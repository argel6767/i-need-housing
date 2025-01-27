"use client"
import { isValidEmail, sleep } from "@/app/utils/utils";
import {verifyUser } from "@/endpoints/auths";
import { VerifyUserDto } from "@/interfaces/requests/authsRequests";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Loading } from "./Loading";
import { ResendVerificationEmail } from "./ResendEmailVerification";

export const VerificationCode = () => {
    const router = useRouter();
    const [credentials, setCredentials] = useState<VerifyUserDto> ({
        email: sessionStorage.getItem("email") || "",
        verificationToken:""
    })
    const [isVerified, setIsVerified] = useState<boolean>(false)
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isCallFailed, setIsCalledFailed] = useState<boolean>(false);

    const isEmailSet = !!sessionStorage.getItem("email")

    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCredentials({...credentials, email:event.target.value});
        if (!isValidEmail(credentials.email) && credentials.email.length > 10) {
            console.log("invalid email");
        }
    }

    const handleCodeChange = (event : React.ChangeEvent<HTMLInputElement>) => {
        setCredentials({...credentials, verificationToken:event.target.value.replace(/\D/g, "").slice(0,6)});
    }

    const handleVerificationCall = async () => {
        setIsLoading(true);
        const data = await verifyUser(credentials);
        setIsLoading(false);
        if (data === "User verified!") {
            setIsVerified(true);
            await sleep(1500)
            router.push("/sign-in");
        }
        if (data.includes("Something went wrong and the api call failed")) {
            setIsCalledFailed(true);
            await sleep(1500);
            setIsCalledFailed(false);
        }
        if (data === "User has already been verified!") {
            setIsVerified(true);
            await sleep(1500);
            router.push("/sign-in");
        }
    }


    if (isLoading) {
        return (
            <Loading loadingMessage={"Verifying User"}/>
        )
    }

    if (isVerified) {
        return (
            <section className="flex flex-col gap-2 text-center mt-3">
                <h1 className="text-lg font-semibold">Account Verified!</h1>
                <p>Your account has been successfully verified. You will be redirected to the login page shortly.</p>
            </section>
        )
    }

    if (isCallFailed) {
        return (
            <section className="flex flex-col gap-2">
                <h1 className="flex-1 text-xl text-red-600 font-bold">Something went wrong! Please try again.</h1>
                <p className="text-lg">The following may be at fault:</p>
                <span className="px-2">
                    <ul className="list-disc">
                        <div className="space-y-1 italic">
                            <li>The Backend server is currently down.</li>
                            <li>The account the email is attached to is already verified.</li>
                            <li>The email or verification code is invalid.</li>
                        </div>
                    </ul>
                </span>
            </section>
        )
    }
    

    return (
        <main className="mt-5">
           <form>
            <div className="flex flex-col gap-3">
                <label className="text-base font-medium text-gray-900">
                    Email
                </label>
                <div className="mt-2">
                    <input
                    placeholder="Email"
                    type="email"
                    className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                    name="email"
                    value={credentials.email}
                    onChange={handleEmailChange}
                    disabled={isEmailSet}
                    />
                </div>
                <label className="text-base font-medium text-gray-900">
                    Verification Code
                </label>
                <div className="mt-2">
                    <input
                    placeholder="Code"
                    pattern="\d*"
                    inputMode="numeric"
                    className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50 text-center"
                    name="email"
                    value={credentials.verificationToken}
                    onChange={handleCodeChange}
                    maxLength={6}
                    />
                </div>
                <button
                className="inline-flex w-full items-center justify-center rounded-md bg-primary hover:bg-[#457F9F] px-3.5 py-2.5 mt-2 font-semibold leading-7 text-white"
                type="button"
                onClick={handleVerificationCall}
            >
                Verify Account
            </button>
            </div>
        </form>
            <ResendVerificationEmail email={credentials.email} message={"Need another code?"} button={"Request another."}/>
        </main>
    )
}