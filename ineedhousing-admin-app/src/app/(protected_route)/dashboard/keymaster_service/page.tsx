import {Navbar} from "@/components/navbar";
import {LogStreamViewer, PingService} from "@/components/service-functions";

export default function KeymasterServicePage() {
    return (
        <main className="h-screen overflow-clip">
            <Navbar />
            <h1 className={"text-5xl  text-center py-4"}>Cron Job Service</h1>
            <span className={"flex h-full w-full p-4"}>
                <section className={"w-4/6"}>
                <LogStreamViewer serviceEndpoint={"/keymaster/logs"} service={"Keymaster Service"} />
            </section>
            <section className={"w-2/6 flex flex-col items-center "}>
                <PingService service={"Keymaster Service"}/>
            </section>
            </span>
        </main>
    )
}