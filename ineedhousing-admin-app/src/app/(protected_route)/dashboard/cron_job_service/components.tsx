'use client'
import {useToggle} from "@/hooks/use-toggle";
import {useRef, useState} from "react";
import {triggerJob} from "@/api/cron-job";
import {sleep} from "@/lib/utils";

interface JobStatusProps {
    status: string;
}
export const JobsStatus = ({status}: JobStatusProps) => {
    const {value, toggleValue} = useToggle(true);

    return (
        <main>
            <h1>Last {status} job</h1>
        </main>
    )
}

interface JobTrigger {
    jobDescription: string;
    apiRequest: () => Promise<string>;

}

interface JobTriggerProps {
    jobTrigger: JobTrigger;
}

const JobTriggerButton = ({jobTrigger}: JobTriggerProps) => {
    const [responseMessage, setResponseMessage] = useState<string>("");
    const {value, toggleValue} = useToggle(false);

    const triggerJob = async () => {
        toggleValue();
        const response = await jobTrigger.apiRequest();
        setResponseMessage(response);
        toggleValue();
        await sleep(3000);
        setResponseMessage("");
    }

    if (responseMessage.length > 0) {
        return (
            <div className={"bg-slate-300 text-black  rounded-full p-3 hover:bg-slate-200 hover:cursor-pointer text-center"}>{responseMessage}</div>
        )
    }

    if (value) {
        return (
            <button className={"bg-slate-300 text-black text-lg rounded-full p-2 hover:bg-slate-200 hover:cursor-pointer text-center"} disabled={true}><span className="loading loading-dots loading-md"></span></button>
        )
    }

    return (
        <button className={"bg-slate-300 text-black text-lg rounded-full p-2 hover:bg-slate-200 hover:cursor-pointer text-center"} onClick={triggerJob}>{jobTrigger.jobDescription}</button>
    )
}

export const ManualJobTriggers = () => {

    const jobs: JobTrigger[] = [
        {
            jobDescription:"Delete stale INeedHousing API images",
            apiRequest: async () => {return await triggerJob("i-need-housing_image");}
        },
        {
            jobDescription:"Delete stale Cron Job Service images",
            apiRequest: async () => {return await triggerJob("cron-job_image");}
        },
        {
            jobDescription:"Delete stale New Listings Service images",
            apiRequest: async () => {return await triggerJob("new-listings_image");}
        },
        {
            jobDescription:"Delete old housing listings from database",
            apiRequest: async () => {return await triggerJob("old-listings");}
        },
        {
            jobDescription:"Trigger registration key rotation",
            apiRequest: async () => {return await triggerJob("key-rotation");}
        }
    ]

    const dialogRef = useRef<HTMLDialogElement>(null);

    const openModal = () => {
        if (dialogRef.current) {
            dialogRef.current.showModal();
        }
    }

    const closeModal = () => {
        if (dialogRef.current) {
            dialogRef.current.close();
        }
    }

    return (
        <main>
            <div className={"p-4 flex flex-col items-center justify-center bg-slate-200 rounded-lg shadow-lg gap-4"}>
                <button className="btn bg-primary rounded-lg" onClick={openModal}>Trigger Cron Job</button>
            </div>
            <dialog className="modal" ref={dialogRef}>
                <div className="modal-box bg-primary-light rounded-lg text-white shadow-lg">
                    <h3 className="font-bold text-2xl italic">Trigger A Cron Job</h3>
                    <ul className={"flex flex-col gap-4 pt-5"}>
                        {jobs.map((job: JobTrigger, index) => (
                            <JobTriggerButton jobTrigger={job} key={index}/>
                        ))}
                    </ul>
                    <div className="modal-action">
                        <button className="btn rounded-full bg-slate-300 text-black" onClick={closeModal}>Close</button>
                    </div>
                </div>
            </dialog>
        </main>
    )
}