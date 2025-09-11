'use client'


import {useState} from "react";
import {useRouter} from "next/navigation";
import {useToggle} from "@/hooks/use-toggle";
import {login} from "@/api/authenication";

interface FailedState {
    errorMessage: string;
    isFailed: boolean;
}

const Form = () => {

    const {value: isLoading, toggleValue: toggleLoading} = useToggle(false);
    const [failedState, setFailedState] = useState<FailedState>({errorMessage: '', isFailed: false});
    const [formData, setFormData] = useState<AuthenticateDto>({email: '', password: ''});
    const router = useRouter();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
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

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        toggleLoading();
        const response = await login(formData);
        
        const userData = response;
        localStorage.setItem("user", JSON.stringify(userData));
        router.push("/chats");
    }

}