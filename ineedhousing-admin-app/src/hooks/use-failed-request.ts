import { useState } from "react";

export const useFailedRequest = () => {

    const [failedRequest, setFailedRequest] = useState({isFailed: false, message: ""});

    const updateFailedRequest = (isFailed: boolean, message:string) => setFailedRequest({isFailed:isFailed, message: message})

    const setRequestFailed = (failureMessage: string) => {
        setFailedRequest({isFailed:true, message: failureMessage});
    }

    const resetFailedRequest = () => setFailedRequest({isFailed: false, message: ""})

    return {failedRequest, updateFailedRequest, resetFailedRequest, setRequestFailed}
}
