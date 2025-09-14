import {LogStreamViewer} from "@/components/service-functions";
import {Navbar} from "@/components/navbar";

export default function CronJobServicePage()  {
    return (
        <main className="h-screen">
            <Navbar />
            <span className={"flex h-full w-full p-2"}>
                <section className={"w-3/6"}>
                <LogStreamViewer serviceEndpoint={"/cron_job/logs"} service={"Cron Job Service"}/>
            </section>
            <section className={"w-3/6 flex flex-col items-center justify-center"}>
                <h1>Some stuff</h1>
                <h2>SOme more stuff</h2>
            </section>
            </span>

        </main>
    )
}