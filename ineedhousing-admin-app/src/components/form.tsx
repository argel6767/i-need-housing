'use client'


import {ChangeEvent, FormEvent, useState} from "react";
import {useRouter} from "next/navigation";
import {useToggle} from "@/hooks/use-toggle";
import {login} from "@/api/authenication";
import {useFailedRequest} from "@/hooks/use-failed-request";
import {sleep} from "@/lib/utils";

const Form = () => {

    const {value: isLoading, toggleValue: toggleLoading} = useToggle(false);
    const {resetFailedRequest, failedRequest, setRequestFailed} = useFailedRequest();
    const [formData, setFormData] = useState<AuthenticateDto>({username: '', password: ''});
    const router = useRouter();

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const resetPasswordInput = () => {
        setFormData((prevState) => ({
            ...prevState,
            password:""
        }));
    }

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        toggleLoading();
        const response = await login(formData);

        if (response === "User logged in") {
            router.push("/dashboard");
        }
        else {
            setRequestFailed(response);
            toggleLoading();
            await sleep(3000);
            resetFailedRequest();
            resetPasswordInput();
        }

    }

    const isInputEmpty = () => {
        return formData.username.length === 0 && formData.password.length === 0;
    }

    if (isLoading) {
        return (
            <main className={"flex flex-col items-center justify-center w-full bg-slate-200 text-black gap-4 py-4 max-w-sm rounded-xl shadow-xl"}>
                <span className="loading loading-spinner loading-xl"></span>
                <p>Authenticating...</p>
            </main>
        )
    }

    if (failedRequest.isFailed) {
        return (
            <main className={"flex flex-col items-center justify-center w-full bg-slate-200 text-black gap-4 py-4 max-w-sm rounded-xl shadow-xl"}>
                <h2 className={"text-xl"}>Something went wrong!</h2>
                <p>{failedRequest.message}</p>
            </main>
        )
    }

    return (
        <form onSubmit={handleSubmit}
              className={"flex flex-col items-center justify-center card-xl shadow-lg bg-slate-200 min-w-sm rounded-lg"}>
            <span className={"flex flex-col items-center justify-center p-4 gap-3"}>
                <fieldset className="fieldset">
                    <legend className="fieldset-legend text-black">Email</legend>
                    <input type="email" name={"username"} value={formData.username} onChange={handleInputChange} className="input bg-slate-100 text-black" placeholder="example@email.com"/>
                </fieldset>
                <fieldset className="fieldset">
                    <legend className="fieldset-legend text-black">Password</legend>
                    <input type="password" name={"password"} value={formData.password} onChange={handleInputChange} className="input bg-slate-100 text-black" placeholder="******"/>
                </fieldset>
                <button className={" bg-primary p-2 rounded-lg shadow-lg text-lg hover:bg-[#457F9F] disabled:bg-primary text-white"} type={"submit"} disabled={isInputEmpty()}>Sign In</button>
            </span>
        </form>
    )

}

export default Form;