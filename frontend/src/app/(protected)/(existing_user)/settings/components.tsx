'use client'

import {
    useClearExistingUserContext,
    useClearGlobalContext,
    useClearProtectedContext,
    useDialog,
    useGetUserDetails
} from "@/hooks/hooks";
import {useState} from "react";
import {useRouter} from "next/navigation";
import {deleteUser} from "@/endpoints/users";
import {queryClient, sleep} from "@/utils/utils";
import {Loading} from "@/components/Loading";
import {logout} from "@/endpoints/auths";

const DeleteAccount = () => {
    const {dialogRef, open, close} = useDialog();
    const [isLoading, setIsLoading] = useState(false);
    const [isFailed, setIsFailed] = useState(false);
    const [isDeleted, setIsDeleted] = useState(false);
    const router = useRouter();
    const clearGlobalState = useClearGlobalContext();
    const clearExistingUserState = useClearExistingUserContext();
    const clearProtectedState = useClearProtectedContext();
    
    const deleteAccount = async () => {
        setIsLoading(true);
        try {
            await deleteUser();
            await logout();
        }
        catch (error) {
            setIsLoading(false)
            setIsFailed(true);
            await sleep(4000);
            setIsFailed(false);
            close();
        }
        setIsLoading(false);
        sessionStorage.setItem("status", "signed out");
        sessionStorage.setItem("hasAlreadyPinged", "false");
        sessionStorage.removeItem("userEmail");
        setIsDeleted(true);
        await sleep(3000);
        router.replace("/sign-in");
        queryClient.clear();
        clearGlobalState();
        clearExistingUserState();
        clearProtectedState();
        
    }
    
    if (isFailed) {
        return (<p className={"text-red-500"}>Could not successfully delete your account. Try again later</p>)
    }

    return (
        <main>
            <button className={"btn btn-primary"} onClick={open}>Delete your account</button>
            <dialog ref={dialogRef} className={"modal"}>
                <section className={"modal-box"}>
                    {
                        isDeleted ? (
                            <p>You&#39;re account has been successfully deleted. We&#39;re sad to see you go!</p>
                        ) :
                            (<div className={"flex flex-col gap-4"}>
                                <p className={"text-center text-xl"}>Are you sure? <p className={"font-semibold"}>This cannot be reversed.</p></p>
                                <span className={"flex justify-center gap-2"}>
                                    <button disabled={isLoading} className={"btn btn-primary"} onClick={close}>Cancel</button>
                                    <button disabled={isLoading} className={"btn btn-primary"} onClick={deleteAccount}>{isLoading? <Loading/>: "Confirm"}</button>
                                </span>
                            </div>)
                    }
                </section>
            </dialog>
        </main>
    )
}


export const UserDetails = () => {
    const email = sessionStorage.getItem('userEmail')
    const noEmail = email === null
    const query = useGetUserDetails(email!)

    if (query.isLoading || query.isRefetching) {
        return (
            <div className="flex items-center justify-center h-full">
                <p>Loading...</p>
            </div>
        )
    }

    if (query.isError || noEmail) {
        return (
            <main className="flex flex-col items-center justify-center gap-4 p-6">
                <p>Could not fetch your details.</p>
                <button
                    type="button"
                    onClick={() => query.refetch()}
                    className="px-4 py-2 rounded-md border"
                >
                    Try again
                </button>
            </main>
        )
    }

    const userDetails = query.data!

    return (
        <main className="flex flex-col gap-4 p-6">
            <h2 className="text-xl font-semibold">Your Details</h2>
            <div className="flex flex-col gap-2">
                <div className="flex items-center justify-between">
                    <span className="font-medium">Email:</span>
                    <span>{userDetails.email}</span>
                </div>
                <div className="flex items-center justify-between">
                    <span className="font-medium">Account created:</span>
                    <span>{new Date(userDetails.createdAt).toLocaleString()}</span>
                </div>
                <span className={"py-2 flex justify-center"}>
                    <DeleteAccount/>
                </span>
            </div>
        </main>
    )
}