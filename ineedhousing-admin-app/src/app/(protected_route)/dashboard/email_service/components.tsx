'use client'
import {useDialog} from "@/hooks/use-dialog";
import {ReactNode} from "react";

interface TemplateProps {
    children: ReactNode;
}

export const TemplateCreationContainer = () => {

}

export const CreateNewEmailTemplate = ({children}: TemplateProps) => {
    const {dialogRef, open, close} = useDialog();
    return (
        <main>
            <div className={"p-4 flex flex-col items-center justify-center bg-slate-200 rounded-lg shadow-lg gap-4"}>
                <button className="btn bg-primary rounded-lg" onClick={open}>Create New Email Template</button>
            </div>
            <dialog ref={dialogRef} className={"modal"}>
                <section className={"modal-box bg-slate-200 rounded-lg shadow-lg"}>
                    {children}
                </section>
                <div className="modal-action">
                    <button className="btn rounded-full bg-slate-300 text-black" onClick={close}>Close</button>
                </div>
            </dialog>
        </main>
    )
    }

    export const EditEmailTemplate = ({children}: TemplateProps) => {
        const {dialogRef, open, close} = useDialog();

        return (
            <main>
                <div className={"p-4 flex flex-col items-center justify-center bg-slate-200 rounded-lg shadow-lg gap-4"}>
                    <button className="btn bg-primary rounded-lg" onClick={open}>Update an Email Template</button>
                </div>
                <dialog ref={dialogRef} className={"modal"}>
                    <section className={"modal-box bg-slate-200 rounded-lg shadow-lg"}>
                        {children}
                    </section>
                    <div className="modal-action">
                        <button className="btn rounded-full bg-slate-300 text-black" onClick={close}>Close</button>
                    </div>
                </dialog>
            </main>
        )
}