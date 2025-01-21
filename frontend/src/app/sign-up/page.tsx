"use client"
import { Form } from "@/components/Form";
import { FormHeader } from "@/components/FormHeader";
import { useEffect, useRef } from "react";

const SignUp = () => {
    
    const videoRef: any = useRef(null);

    useEffect( () => {
        if(videoRef.current) {
            videoRef.current.play();
        }
    }, [])

    return ( 
        <main className="h-screen items-center justify-center">
                    <section className="rounded-md p-2 bg-white items-center">
                        <div className="flex items-center justify-center my-3">
                        <div className="xl:mx-auto shadow-md p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                            <FormHeader header="Sign up to create account" text="Already have an account? " buttonLabel="Sign In" path="sign-in"/>
                            <Form buttonLabel="Create Account"/>
                        </div>
                        </div>
                    </section>
        </main>
    )
}

export default SignUp;