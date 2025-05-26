"use client"
import { resendVerificationEmail } from "@/endpoints/auths"
import { useState } from "react"

interface props {
    email:string
    message:string
    button:string
}
export const ResendVerificationEmail = ({email, message, button}:props) => {
    const [isCalledFailed, setIsCalledFailed] = useState<boolean>(false)

    const handleResendRequest = async() => {
        await resendVerificationEmail({email: email});
    }

    return (
        <p className="p-4 animate-fade">{message} <button onClick={handleResendRequest} className="hover:underline">{button}</button></p>
    )
}