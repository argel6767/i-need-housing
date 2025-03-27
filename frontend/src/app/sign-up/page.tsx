"use client"
import { Footer } from "@/components/Footer";
import { Form } from "@/components/Form";
import { FormHeader } from "@/components/FormHeader";
import Link from "next/link";
import Image from "next/image";
import icon from "../../../public/file.svg"
import { register } from "@/endpoints/auths";



const SignUp = () => {

    return ( 
        <main className="h-screen flex flex-col items-center justify-between">
            <Link href={"/"} className="flex items-center text-primary text-4xl sm:text-5xl md:text-6xl font-semibold gap-2 pt-5 cursor:pointer">INeedHousing<Image src={icon} alt="Logo" width={40} height={40}/></Link>    
                <section className="rounded-md p-2 bg-white items-center">
                    <div className="flex items-center justify-center my-3">
                    <div className="xl:mx-auto shadow-md p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                        <FormHeader header="Sign up to create account" text="Already have an account? " buttonLabel="Sign In" path="sign-in"/>
                        <Form buttonLabel="Create Account" loadingMessage="Registering User" route="/sign-up/verify" request={register}/>
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

