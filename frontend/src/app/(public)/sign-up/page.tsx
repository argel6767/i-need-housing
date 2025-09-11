"use client"
import { Footer } from "@/components/Footer";
import { Form} from "@/components/Form";
import { FormHeader } from "@/components/FormHeader";
import Link from "next/link";
import Image from "next/image";
import icon from "../../../../public/file.svg"
import {registerV2} from "@/endpoints/auths";
import {useState} from "react";
import {useRouter} from "next/navigation";
import {AuthenticateUserDto} from "@/interfaces/requests/authsRequests";
import {sleep} from "@/utils/utils";



const SignUp = () => {
    const [errorState, setErrorState] = useState({
        isError: false,
        message: ""
    });
    const router = useRouter();
    const [isLoading, setIsLoading] = useState(false);

    const signUpUser = async (credentials: AuthenticateUserDto) => {
        const response = await registerV2(credentials);
        if (response.message === "user created") {
            sessionStorage.setItem("email", response.email)
            router.push("/sign-up/verify");
        }
        else {
            setErrorState({ isError: true, message: response });
            await sleep(3000);
            setErrorState({ isError: false, message: "" });
        }
    }


    return ( 
        <main className="h-screen flex flex-col items-center justify-between">
            <Link href={"/"} className="flex items-center text-primary text-4xl sm:text-5xl md:text-6xl font-semibold gap-2 pt-5 cursor:pointer">INeedHousing<Image src={icon} alt="Logo" width={40} height={40}/></Link>    
                <section className="rounded-md p-2 bg-white items-center">
                    <div className="flex items-center justify-center my-3">
                    <div className="xl:mx-auto shadow-md p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                        <FormHeader header="Sign up to create account" text="Already have an account? " buttonLabel="Sign In" path="sign-in"/>
                        <Form buttonLabel={"Create Account"} request={signUpUser} isLoading={isLoading} formType={"signUp"}/>
                        <p className={"text-red-500 text-center font-semibold animate-fade"}>{errorState.message}</p>
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

