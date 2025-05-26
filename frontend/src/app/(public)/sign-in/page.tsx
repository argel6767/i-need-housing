"use client"
import { Footer } from "@/components/Footer";
import { Form} from "@/components/Form";
import { FormHeader } from "@/components/FormHeader";
import Link from "next/link";
import Image from "next/image";
import icon from "../../../../public/file.svg"
import { login } from "@/endpoints/auths";
import {useGlobalContext} from "@/components/GlobalContext";
import {AuthenticateUserDto} from "@/interfaces/requests/authsRequests";
import {useState} from "react";
import {useRouter} from "next/navigation";
import {sleep} from "@/utils/utils";
import {ResendVerificationEmail} from "@/components/ResendEmailVerification";

const SignIn = () => {
    const {setIsFirstTimeUser} = useGlobalContext();
    const [errorState, setErrorState] = useState({
        isError: false,
        message: ""
    });
    const router = useRouter();
    const [isRequestingEmail, setIsRequestingEmail] = useState<boolean>(false);
    const [email, setEmail] = useState("")
    const [isLoading, setIsLoading] = useState(false)

    const loginUser = async (credentials: AuthenticateUserDto) => {
        setIsLoading(true);
        const response = await login(credentials);
        setIsLoading(false);
        if (response === "new user") {
            setIsFirstTimeUser(true);
            router.prefetch("/new-user/user-type")
            router.push("/new-user/user-type");
        }
        else if (response === "logged in") {
            router.prefetch("/home")
            router.push("/home");
        }
        else if (response.message === "user is not verified") {
            setIsRequestingEmail(true);
            setEmail(response.email)
        }
        else {
            setErrorState({ isError: true, message: response });
            await sleep(1500);
            setErrorState({ isError: false, message: "" });
        }
    }

    const handleResendHref = (e:any) => {
        e.preventDefault();
        router.push("/sign-up/verify");
    }

    return (
            <main className="h-screen flex flex-col items-center justify-between">
                <Link href={"/"} className="flex items-center text-primary text-4xl sm:text-5xl md:text-6xl font-semibold gap-2 pt-5 cursor:pointer">INeedHousing<Image src={icon} alt="Logo" width={40} height={40}/></Link>
                <section className="rounded-md p-2 bg-white items-center">
                    <div className="flex items-center justify-center my-3">
                    <div className="xl:mx-auto shadow-lg rounded-lg p-4 xl:w-full xl:max-w-sm 2xl:max-w-md">
                        <FormHeader header="Sign in to INeedHousing" text="Not already a member? " buttonLabel="Sign Up" path="/sign-up"/>
                        <Form buttonLabel="Sign In" request={loginUser} isLoading={isLoading} />
                        <div className={`font-semibold ${isRequestingEmail ? "display" : "hidden"}`  } onClick={handleResendHref}>
                            <ResendVerificationEmail email={email} message={"Your account is not verified."} button={"Request code."}/>
                        </div>
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

export default SignIn;