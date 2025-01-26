"use client"
import { resendVerificationEmail } from "@/endpoints/auths"
import { useState } from "react"

interface props {
    email:string
}
export const ResendVerificationEmail = ({email}:props) => {
    const [isCalledFailed, setIsCalledFailed] = useState<boolean>(false)

    const handleResendRequest = async() => {
        await resendVerificationEmail({email: email});
    }

    return (
        <p className="p-4">Need another code? <button onClick={handleResendRequest} className="hover:underline">Request another</button></p>
    )
}