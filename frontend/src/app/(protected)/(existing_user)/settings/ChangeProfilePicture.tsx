'use client'

import {useExistingUserContext} from "@/app/(protected)/(existing_user)/ExistingUserContext";
import {ProfilePicture} from "@/components/User";

export const ChangeProfilePicture = () => {
    const {profilePictureUrl, setProfilePictureUrl} = useExistingUserContext();

    if (!profilePictureUrl && !setProfilePictureUrl) {
        return (<div>Loading..</div>);
    }

    return (
        <main className={"flex flex-col justify-center items-center w-full"}>
            <div className="avatar">
                <div className="w-40 md:w-50 rounded-full shadow-2xl">
                    <ProfilePicture />
                </div>
            </div>

        </main>
    )
}