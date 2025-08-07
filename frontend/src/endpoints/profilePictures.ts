import {apiClient} from "@/endpoints/apiConfig";
import axios from "axios";

const MODULE_MAPPING = "/profile-pictures";

/**
 * gets profile picture url from UserProfilePicture Entity
 */
export const getProfilePictureURL = async (): Promise<string> => {
    try {
        const response = await apiClient.get(MODULE_MAPPING+"/url");
        return response.data;
    }
    catch (error:any) {
        if (error.response.status === 404) {
            return "user does not have profile picture";
        }
    }
    return "Call failed!"
}

/**
 * Updates the UserProfilePicture due to its expiration
 */
export const updateProfilePictureURL = async (): Promise<string> => {
    try {
        const response = await apiClient.put(MODULE_MAPPING+"/url");
        return response.data;
    }
    catch (error:any) {
        if (error.response.status === 404) {
            return "user does not have profile picture";
        }
    }
    return "Called failed!"
}

/**
 * uploads the users profile picture
 * @param profilePicture
 */
export const updatePicture = async (profilePicture: Blob): Promise<string> => {
    const formData = new FormData();
    formData.append("file", profilePicture);
    try {
        const response = await apiClient.put(MODULE_MAPPING+"/upload", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        });
        return response.data;
    }
    catch (error:any) {
        if (error.response.status === 404) {
            return "user does not have profile picture, create an initial profile picture";
        }
        else if (error.response.status === 500) {
            return "server error, try again later";
        }
        else if (error.response.status === 400) {
            return error.message;
        }
        else if (error.response.status === 413) {
            return "file is too large!"
        }
    }
    return "Called failed!";
}

/**
 * creates a brand new entry in UserProfilePicture table
 * @param profilePicture
 */
export const createUserProfilePicture = async (profilePicture: Blob): Promise<string> => {
    const formData = new FormData();
    formData.append("file", profilePicture);
    try {
        const response = await apiClient.post(MODULE_MAPPING, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        });
        return response.data;
    }
    catch (error:any) {
        if (error.response.status === 404) {
            return "user does not have profile picture";
        }
        else if (error.response.status === 500) {
            return "server error";
        }
        else if (error.response.status === 400) {
            return error.message;
        }
        else if (error.response.status === 413) {
            return "file is too large!"
        }
    }
    return "Called failed!";
}

export const getProfilePicture = async (profilePictureURL: string): Promise<string> => {
    try {
        const response = await axios.get(profilePictureURL, {
            responseType: 'blob'
        })
        const imageURL = URL.createObjectURL(response.data);
        return imageURL;
    }
    catch (error:any) {
        if (error.response.status === 403) {
            const updatedUrl =  await updateProfilePictureURL();
            return updatedUrl;
        }
        return Promise.reject(error.message);
    }
}

export const deleteProfilePicture = async () => {
    try {
        await apiClient.delete(MODULE_MAPPING);
        return "profile picture deleted!";
    }
    catch (error: any) {
        console.error(error.response)
        return "profile picture failed to delete"
    }
}