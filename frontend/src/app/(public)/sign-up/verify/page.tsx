import { Footer } from "@/components/Footer";
import { FormHeader } from "@/components/FormHeader";
import Link from "next/link";
import Image from "next/image";
import icon from "../../../../../public/file.svg"
import { VerificationCode } from "@/components/VerificationCode";
import {BackButton} from "@/components/back";
import React from "react";



const Verify = () => {
    return ( 
        <main className="h-screen flex flex-col items-center justify-between">
            <nav className={"flex justify-start w-full"}>
                <BackButton backPath={"/"}/>
            </nav>
            <section className="rounded-md p-2 bg-white items-center">
                <div className="flex items-center justify-center my-3">
                    <div className="xl:mx-auto shadow-md p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                        <FormHeader header="Verify your email" text="Already have an account? " buttonLabel="Sign In" path="/sign-in"/>
                        <VerificationCode/>
                    </div>
                    </div>
                </section>
                <div className="w-full border-t-2">
                    <Footer/>
                </div>
        </main>
    )
}

export default Verify;