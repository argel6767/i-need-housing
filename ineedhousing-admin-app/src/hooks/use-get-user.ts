import {useEffect, useState} from "react";
import {UserDto} from "@/lib/models";

export const useGetUser = () => {
    const [user, setUser] = useState<UserDto | null>(null);

    useEffect(() => {
        const userString = sessionStorage.getItem("user");
        if (userString) {
            try {
                setUser(JSON.parse(userString));
            } catch {
                setUser(null);
            }
        } else {
            setUser(null);
        }
    }, []);

    return {user};
}