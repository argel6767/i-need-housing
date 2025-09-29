'use client'

import {useToggle} from "@/hooks/use-toggle";
import {ChangeEvent, FormEvent, useEffect, useRef, useState} from "react";
import {RegisteredServiceDto} from "@/lib/models";
import {registerService} from "@/api/keymaster";
import {CopyButton} from "@/components/copy-button";

export const RegisterNewService = () => {
    const {value: loading, toggleValue: toggleLoading} = useToggle(false);
    const [serviceName, setServiceName] = useState<string>("")
    const [registeredService, setRegisteredService] = useState<RegisteredServiceDto>()
    const dialogRef = useRef<HTMLDialogElement>(null);

    useEffect(() => {
        if (registeredService && dialogRef.current) {
            dialogRef.current.showModal();
        }
    }, [registeredService]);

    const handleServiceNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        setServiceName(e.target.value);
    }

    const closeModal = () => {
        if (dialogRef.current) {
            dialogRef.current.close();
        }
    }

    const handleServiceRegistration = async (e: FormEvent) => {
        e.preventDefault();
        toggleLoading();
        const data = await registerService(serviceName);
        toggleLoading();

        if (data.apiToken !== "Failed to register new service") {
            setRegisteredService(data);
            setServiceName("")
        }
    }

    const didServiceRegistrationFailed = () => {
        return registeredService && registeredService.apiToken === "Failed to register new service";
    }

    if (loading) {
        return (
            <main className={"flex flex-col items-center justify-center gap-5 p-4 bg-slate-200 rounded-lg shadow-lg text-xl min-w-xs"}>
                <h1>Registering {serviceName}</h1>
                <span className="loading loading-dots loading-xl"></span>
            </main>
        )
    }

    if (didServiceRegistrationFailed()) {
        return (
            <main className={"flex flex-col items-center justify-center gap-5 p-4 bg-slate-200 rounded-lg shadow-lg text-xl min-w-xs"}>
                <h1>{serviceName} could not be registered</h1>
                <p>{registeredService?.message}</p>
            </main>
        )
    }

    return (
        <main
            className={"flex flex-col items-center justify-center gap-5 p-4 bg-slate-200 rounded-lg shadow-lg text-xl min-w-xs"}>
            <h1>Register a new service</h1>
            <form onSubmit={handleServiceRegistration} className={"flex flex-col items-center justify-center gap-4"}>
                <input type="text" name={"username"} value={serviceName} onChange={handleServiceNameChange} className="input bg-slate-100 text-black" placeholder="Coolest service ever"/>
                <button className="btn rounded-full bg-primary text-white" type={"submit"}>Register Service</button>
            </form>
            <dialog ref={dialogRef} className="modal">
                <div className={"flex flex-col items-center justify-center gap-4 bg-slate-200 rounded-lg shadow-lg p-6"}>
                    <h2 className={"text-2xl"}>{registeredService?.serviceName} has been successfully registered</h2>
                    <h3 className={"text-lg flex gap-1"}>Please copy and save the following details for <p className={"italic"}>{registeredService?.serviceName}.</p>Do not lose
                        them!</h3>
                    <ul className={"flex flex-col gap-4"}>
                        <li>Service Name: 
                            <input type={"text"} className="input bg-slate-100 text-black disabled:bg-slate-100 disabled:text-black" disabled={true} value={registeredService?.serviceName}/>
                            <CopyButton value={registeredService?.serviceName || "No service name given"} />
                        </li>
                        <li>Service API Token: 
                            <input type={"text"} className="input bg-slate-100 text-black disabled:bg-slate-100 disabled:text-black" disabled={true} value={registeredService?.apiToken}/>
                            <CopyButton value={registeredService?.apiToken || "No service API token given"}/>
                        </li>
                    </ul>
                    <div className="modal-action">
                        <button className="btn rounded-full bg-primary text-white" onClick={closeModal}>Close</button>
                    </div>
                </div>
            </dialog>
        </main>
    )

}