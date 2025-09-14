
"use client";

import { useWebSocket } from '@/hooks/use-web-socket';
import {useToggle} from "@/hooks/use-toggle";
import {useEffect, useRef} from "react";

type LogEvent = {
    timeStamp: string;
    level: string;
    log: string;
};

interface LogStreamViewerProps {
    serviceEndpoint: string;
    service: string;
}

export const LogStreamViewer = ({ serviceEndpoint, service }: LogStreamViewerProps)=> {

    const { messages, connect, isConnected, close } = useWebSocket(serviceEndpoint);
    const {value: isOpenStream, toggleValue: toggleIsOpenStream } = useToggle(false);
    const listRef = useRef<HTMLUListElement>(null);

    useEffect(() => {
        if (listRef.current) {
            listRef.current.scrollTop = listRef.current.scrollHeight;
        }
    }, [messages]);

    const formatMessage = (message: LogEvent, idx: number) => {
        const date = new Date(message.timeStamp).toLocaleString();
        return (<li key={idx}>[{date}] {message.level}: {message.log}</li>)
    }

    const connectToStream = () => {
        toggleIsOpenStream()
        connect();
    }

    const closeStream = () => {
        toggleIsOpenStream()
        close();
    }

    if (!isConnected && isOpenStream) {
        return (
            <div className={"p-4 rounded-box shadow-sm bg-slate-200 w-full h-5/6 flex flex-col items-center justify-center"}>
                <span className="loading loading-bars loading-xl w-60"></span>
            </div>

        )
    }

    if (!isOpenStream) {
        return (
            <div className="p-4 rounded-box shadow-sm flex justify-center items-center text-center bg-slate-200 flex flex-col items-center justify-center w-full h-5/6">
                <button onClick={connectToStream} className={"btn-md bg-primary hover:bg-primary-light p-3 rounded-lg shadow-lg text-white"}>{`Watch ${service} log stream`}</button>
            </div>
        )
    }

    return (
        <div className="p-4 rounded-box shadow-sm flex justify-center items-center text-center bg-slate-200 flex flex-col items-center justify-center w-full h-5/6">
            <nav className={"flex justify-between items-center p-4 w-full"}>
                <h2 className="font-bold mb-2">{`${service} Live Logs:`}</h2>
                <button onClick={closeStream} className={"btn-md bg-primary hover:bg-primary-light p-2 rounded-lg shadow-lg text-white"}>Close Stream</button>
            </nav>
            <ul className="bg-black text-green-400 p-6 h-full overflow-y-auto rounded-lg shadow-lg w-full text-left flex flex-col-reverse" ref={listRef}>
                    {messages.map((log, idx) => (formatMessage(log, idx)))}
            </ul>
        </div>
);
}

interface PingServiceProps {
    service: string;
}

export const PingService({service}: PingServiceProps)=> {

    const pingService = async () => {

    }

}