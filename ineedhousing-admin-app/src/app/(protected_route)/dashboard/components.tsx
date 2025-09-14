'use client'
import {useGetUser} from "@/hooks/use-get-user";

export const WelcomeMessage = () => {
    const {user} = useGetUser();

    const getGreetingMessage = () => {
        const hour = new Date().getHours();
        let hourMessage = '';
        if (hour < 12) hourMessage = "Good morning";
        else if (hour < 18) hourMessage = "Good afternoon";
        else hourMessage = "Good evening";

        return `${hourMessage} ${user?.email ?? "admin"}`;
    };

    return (
        <h1 className={"text-4xl font-semibold"}>{getGreetingMessage()}</h1>
    )
}