'use client'
import {useDialog} from "@/hooks/use-dialog";
import {ChangeEvent, Dispatch, FormEvent, ReactNode, SetStateAction, useState} from "react";
import {HtmlRenderer} from "@/components/html-renderer";
import {useToggle} from "@/hooks/use-toggle";
import {generateEmailTemplate} from "@/api/email-service";

interface TemplateProps {
    children: ReactNode;
}


interface TemplateInputProps {
    html: string;
    setHtml: Dispatch<SetStateAction<string>>;
}

const TemplateInput = ({html, setHtml}: TemplateInputProps) => {
    return (
        <main className={"flex items-center justify-center size-full p-2"}>
            <textarea onChange={(e) => setHtml(e.target.value)} value={html} className={"size-full"}/>
        </main>
    )
}

export const CreateNewEmailTemplate = ({children}: TemplateProps) => {
    const {dialogRef, open, close} = useDialog();
    return (
        <main>
            <div className={"p-4 flex flex-col items-center justify-center bg-slate-200 rounded-lg shadow-lg gap-4"}>
                <button className="btn bg-primary rounded-lg" onClick={open}>Create New Email Template</button>
            </div>
            <dialog ref={dialogRef} className={"modal pt-10"}>
                <section className={"modal-box bg-slate-200 rounded-lg shadow-lg min-w-7xl h-[800px]"}>
                    {children}
                </section>
                <div className="modal-action pb-4">
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

interface TemplateAIPrompterProps {
    setHtml: Dispatch<SetStateAction<string>>;
}

const TemplateAIPrompter = ({setHtml}: TemplateAIPrompterProps) => {
    const [input, setInput] = useState("");
    const {value:loading, toggleValue:toggleLoading} = useToggle(false);

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
    }

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        toggleLoading()
        const promptResponse = await generateEmailTemplate(input);
        setHtml(promptResponse);
        toggleLoading()
    }

    if (loading) {
        return (
            <main className={"flex items-center justify-center gap-2"}>
                <div className={"input bg-slate-100 text-gray-950 flex justify-center"}>
                    <span className="loading loading-dots loading-xl"></span>
                </div>
                <button disabled={true} className={"btn"}>Send</button>
            </main>
        )
    }

    return (
        <form className={"flex justify-center items-center gap-2"} onSubmit={handleSubmit}>
            <input type={"text"} placeholder={"Generate email template with AI"} className={"input bg-slate-100 text-gray-950"}
                   onChange={handleInputChange} value={input}/>
            <button type="submit" className={"btn bg-primary rounded-lg hover:bg-primary-light"}>Send</button>
        </form>
    )

}

interface TemplateContainerProps {
    title: string;
}

export const TemplateContainer = ({title}: TemplateContainerProps) => {
    const [html, setHtml] = useState<string>('');

    return (
        <main className={"flex items-center justify-center bg-slate-200 rounded-lg shadow-lg w-full h-full gap-4 text-gray-950"}>
            <section className={"w-3/6 h-full bg-slate-300 rounded-lg shadow-lg flex flex-col gap-2 pb-2"}>
                <TemplateInput html={html} setHtml={setHtml} />
                <TemplateAIPrompter setHtml={setHtml}/>
            </section>
            <section className={"w-3/6 h-full bg-slate-300 rounded-lg shadow-lg overflow-y-scroll"}>
                <h1 className={"text-2xl text-center font-semibold border-b-1 border-b-black"}>Live Preview</h1>
                <HtmlRenderer content={html}/>
            </section>

        </main>
    )
}
