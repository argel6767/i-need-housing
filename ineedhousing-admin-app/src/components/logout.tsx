'use client'

import {useToggle} from "@/hooks/use-toggle";
import {logout} from "@/api/authenication";
import {useRouter} from "next/navigation";
import {queryClient} from "@/lib/utils";
import {Loader} from "lucide-react";

export const Logout = () => {
    const {value: isLoading, toggleValue: toggleLoading} = useToggle(false);
    const router = useRouter();

    const logoutUser = async () => {
        toggleLoading();
        await logout();
        queryClient.clear();
        toggleLoading();
        router.push("/");
    }

    return (
        <button onClick={logoutUser}>Logout <Loader size={22} className={`ml-2 animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
    )
}