'use client'

import {useExistingUserContext} from "@/app/(protected)/(existing_user)/ExistingUserContext";
import {ProfilePicture} from "@/components/User";
import {LoadingBars} from "@/components/Loading";
import {Modal} from "@/components/Modal";
import {DEFAULT_PROFILE_PICTURE_URL, queryClient, sleep} from "@/utils/utils";
import React, { useState } from "react";
import {CircleX, Loader} from "lucide-react";
import {createUserProfilePicture, deleteProfilePicture, updatePicture} from "@/endpoints/profilePictures";

const isValidFileType = (file: File) => {
    return (
        file.type === 'image/png' ||
        file.type === 'image/jpeg' ||
        file.type === 'image/jpg'
    );
}

const isSuccessfulUpload = (response: string ):boolean => {
    return (
        response !== "user does not have profile picture, create an initial profile picture" &&
        response !== "server error, try again later" &&
        response.includes("https://")
    )
}

interface UpdateProfilePictureProps {
    setIsModalUp: React.Dispatch<React.SetStateAction<boolean>>;
    setProfilePictureUrl: React.Dispatch<React.SetStateAction<string>>
    uploadFileFunction: (file:Blob) => Promise<string>
}

const UploadProfilePicture = ({setIsModalUp, setProfilePictureUrl, uploadFileFunction}:UpdateProfilePictureProps) => {
    const [file, setFile] = useState<File>();
    const [isFailure, setIsFailure] = useState<boolean>(false);
    const [errorMessage, setErrorMessage] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isReadyToSubmit, setIsReadyToSubmit] = useState<boolean>(false);

    const handleFileChange = async (event: React.FormEvent) => {
        const files = (event.target as HTMLInputElement).files
        if (files && files.length > 0) {
            const file = files[0];
            if (isValidFileType(file)) {
                setIsFailure(false);
                setIsReadyToSubmit(true);
                setFile(file);
            }
            else {
                setIsReadyToSubmit(false);
                setIsFailure(true);
                setErrorMessage("File must be an image!");
            }
        }
    }

    const uploadProfilePicture = async () => {
        setIsLoading(true);
        const newURL = await uploadFileFunction(file!);
        if (isSuccessfulUpload(newURL)) {
            setProfilePictureUrl(newURL);
            // Invalidate the React Query cache for profile picture queries
            await queryClient.invalidateQueries({ queryKey: ['profilePictureURL'] });
            await queryClient.invalidateQueries({ queryKey: ['profilePicture'] });
            setIsLoading(false);
            setIsModalUp(false);
        }
        else {
            setIsLoading(false);
            setErrorMessage(newURL);
            setIsFailure(true)
            await sleep(1200);
            setIsFailure(false);
        }
    }

        return (
            <main>
                <nav className={"flex justify-end items-end"}>
                    <button disabled={isLoading} onClick={(e) => {e.stopPropagation();setIsModalUp(false);}} className=""><CircleX className="hover:opacity-50" size={40}/></button>
                </nav>
                <article className={"flex flex-col items-center justify-center gap-4"}>
                    <label className="form-control  max-w-xs">
                        <div className="label">
                            <span className="label-text">Upload Your Picture</span>
                        </div>
                        {
                            isLoading ? <LoadingBars /> : (
                                <input disabled={isLoading} type="file"
                                       className="file-input file-input-bordered w-full max-w-xs"
                                       onChange={handleFileChange}/>
                            )
                        }
                        <div className="label">
                            <span className="label-text-alt">Photo must be an image (jpg, jepg, png)</span>
                        </div>
                    </label>
                    {isFailure && (
                        <p className={"text-red-500 animate-fade"}>{errorMessage}</p>
                    )}
                    {isReadyToSubmit && (
                        <button onClick={uploadProfilePicture} className={"btn btn-primary animate-fade"}>Upload Photo</button>
                    )}
                </article>

            </main>
        )
}

interface NonDefaultProfilePictureButtonsProps {
    setProfilePictureUrl: React.Dispatch<React.SetStateAction<string>>
    handleChangePicture: () => void
}

const NonDefaultProfilePictureButtons = ({setProfilePictureUrl, handleChangePicture}: NonDefaultProfilePictureButtonsProps) => {
    const [isLoading, setIsLoading] = useState(false)
    const [isError, setIsError] = useState(false);


    const handleDelete = async () => {
        const successMessage = "profile picture deleted!";
        setIsLoading(true)
        const response = await deleteProfilePicture();
        if (response === successMessage) {
            // Invalidate the React Query cache for profile picture queries
            await queryClient.invalidateQueries({ queryKey: ['profilePictureURL'] });
            await queryClient.invalidateQueries({ queryKey: ['profilePicture'] });
            setProfilePictureUrl(DEFAULT_PROFILE_PICTURE_URL);
            setIsLoading(false);
        }
        else
            setIsLoading(false);
        setIsError(true);
        await sleep(1500);
        setIsError(false);
    }

    return (
        <main className={"flex flex-col justify-center items-center gap-4 animate-fade"}>
            <div className={"flex justify-center items-center gap-6"}>
                <button disabled={isLoading} onClick={handleChangePicture} className={"btn bg-slate-100 shadow-lg hover:bg-slate-200 font-semibold"}>Change Picture</button>
                <button disabled={isLoading} className={"btn bg-slate-100 shadow-lg hover:bg-slate-200"} onClick={handleDelete}>Delete Picture
                    <Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/>
                </button>
            </div>
            {isError && <p className={"text-red-500 animate-fade"}>Profile Picture failed to delete. Try again later.</p>}
        </main>
    )
}

const ProfilePictureButtons = () => {
    const {profilePictureUrl, setProfilePictureUrl} = useExistingUserContext();
    const [isModalUp, setIsModalUp] = useState(false);



    if (!profilePictureUrl || !setProfilePictureUrl) {
        return (<LoadingBars/>)
    }

    const doesUserHaveProfilePicture = () => {
        return profilePictureUrl !== "https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg";
    }

    const handleChangePicture= async () => {
        setIsModalUp(true);
    }

    const determineUploadFunction = () => {
        let returnFunction;
        if (doesUserHaveProfilePicture()) {
            returnFunction =  updatePicture;
        }
        else {
            returnFunction =  createUserProfilePicture;
        }
        return returnFunction
    }

    return (
        <main>
            {isModalUp && (
                <Modal>
                    <UploadProfilePicture setIsModalUp={setIsModalUp} setProfilePictureUrl={setProfilePictureUrl} uploadFileFunction={determineUploadFunction()}/>
                </Modal>
            )}
            {
            doesUserHaveProfilePicture() ? <NonDefaultProfilePictureButtons setProfilePictureUrl={setProfilePictureUrl} handleChangePicture={handleChangePicture}/>
                : <button onClick={handleChangePicture} className={"btn bg-slate-100 shadow-lg hover:bg-slate-200 animate-fade"}>Upload Picture</button>}
        </main>
    )
}

export const ChangeProfilePicture = () => {

    return (
        <main className={"flex flex-col justify-center items-center w-full gap-6 py-6"}>
            <div className="avatar">
                <div className="w-28 md:w-40 md:w-50 rounded-full shadow-2xl">
                    <ProfilePicture />
                </div>
            </div>
            <span className={"flex justify-center items-center w-full"}>
                <ProfilePictureButtons />
            </span>

        </main>
    )
}