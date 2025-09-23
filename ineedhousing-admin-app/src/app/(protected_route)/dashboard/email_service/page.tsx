import {Navbar} from "@/components/navbar";
import {LogStreamViewer, PingService} from "@/components/service-functions";

export default function EmailServicePage() {
    return (
        <main className="h-screen overflow-clip">
            <Navbar />
            <h1 className={"text-5xl  text-center py-4"}>Email Service</h1>
            <span className={"flex h-full w-full p-4"}>
                <section className={"w-4/6"}>
                <LogStreamViewer serviceEndpoint={"/email_service/logs"} service={"Email Service"}/>
            </section>
            <section className={"w-2/6 flex flex-col items-center "}>
                <PingService service={"Email Service"}/>
            </section>
            </span>
        </main>
    )
}