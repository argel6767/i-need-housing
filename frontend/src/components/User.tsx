'use client'
import {useExistingUserContext} from "@/app/(protected)/(existing_user)/ExistingUserContext";
import {useClearState, useProfilePictureWithURL} from "@/hooks/hooks";
import { useRouter } from "next/navigation";
import {useEffect, useState} from "react";
import {logout} from "@/endpoints/auths";
import {queryClient, sleep} from "@/utils/utils";
import {Loading} from "@/components/Loading";
import Link from "next/link";
import {Loader} from "lucide-react";

/**
 * All profile picture logic encapsulated in this component
 * @constructor
 */
export const ProfilePicture = () => {
    const {profilePictureUrl, setProfilePictureUrl} = useExistingUserContext();
    const profilePictureFetch = useProfilePictureWithURL();

    useEffect(() => {
        if (profilePictureFetch.isFetched && profilePictureFetch.hasProfilePicture && profilePictureFetch.data) {
            setProfilePictureUrl(profilePictureFetch.data);
        }
    }, [profilePictureFetch.isFetched, profilePictureFetch.data, setProfilePictureUrl, profilePictureFetch.hasProfilePicture]);

    const isFetching = () => {
        return profilePictureFetch.isLoading || profilePictureFetch.isFetching || profilePictureFetch.isRefetching || profilePictureFetch.isPending;
    }

    if (isFetching()) {
        return <Loading />;
    }

    return (
        <img alt={"user's profile picture"} src={profilePictureUrl}/>
    )
}

/**
 * Holds the User's profile picture, settings, logout logic
 *
 */
export const User = () => {
    const [isError, setIsError] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const router = useRouter();
    const clearState = useClearState();

    const logoutUser = async () => {
        setIsLoading(true);
        const response = await logout();
        setIsLoading(false);
        if (response === "Logged out successfully") {
            router.push("/sign-in");
            queryClient.clear();
            clearState();
        }
        else {
            setIsError(true);
            await sleep(1700);
            setIsError(false);
        }
    }

    return (
        <div className="flex-none gap-2">
            <div className="dropdown dropdown-end ">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar mr-3 bg-primary shadow">
                    <div className="w-full rounded-full shadow-2xl">
                        <ProfilePicture />
                    </div>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content  rounded-box z-[99] mt-3 w-52 p-2 shadow bg-base-100">
                    <li className={"hover:bg-gray-100 rounded-2xl"}><Link href={"/settings"}>Settings</Link></li>
                    <li className={"hover:bg-gray-100 rounded-2xl"}><button onClick={logoutUser}>Logout <Loader size={22} className={`ml-2 animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
                    </li>
                    {isError && <li className="text-red-500">Could not log out user! Try again</li>}
                </ul>
            </div>
        </div>
    )
}