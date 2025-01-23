"use client"
import { Footer } from "@/components/Footer";
import { Form } from "@/components/Form";
import { FormHeader } from "@/components/FormHeader";
import Link from "next/link";
import Image from "next/image";
import icon from "../../../public/file.svg"


const SignUp = () => {

    return ( 
        <main className="h-screen flex flex-col items-center justify-between">
            <div className="flex justify-center items-center pt-4 space-x-2">
                <Image src={icon} alt="Logo" width={40} height={40}/>
                <Link href={"/"} className="items-center text-primary text-4xl md:text-6xl font-semibold">INeedHousing</Link>
            </div>
                <section className="rounded-md p-2 bg-white items-center">
                    <div className="flex items-center justify-center my-3">
                    <div className="xl:mx-auto shadow-md p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                        <FormHeader header="Sign up to create account" text="Already have an account? " buttonLabel="Sign In" path="sign-in"/>
                        <Form buttonLabel="Create Account"/>
                    </div>
                    </div>
                </section>
                <div className="w-full border-t-2">
                    <Footer/>
                </div>
        </main>
    )
}

export default SignUp;

