import {LogStreamViewer, PingService} from "@/components/service-functions";
import {Navbar} from "@/components/navbar";
import {JobsStatus, ManualJobTriggers} from "@/app/(protected_route)/dashboard/cron_job_service/components";

export default function CronJobServicePage()  {
    return (
        <main className="h-screen overflow-clip">
            <Navbar />
            <h1 className={"text-5xl  text-center py-4"}>Cron Job Service</h1>
            <span className={"flex h-full w-full p-4"}>
                <section className={"w-4/6"}>
                <LogStreamViewer serviceEndpoint={"/cron_job/logs"} service={"Cron Job Service"}/>
            </section>
            <section className={"w-2/6 flex flex-col items-center justify-items-stretch gap-20 p-4"}>
                <PingService service={"Cron Job Service"}/>
                <JobsStatus status={"Successful"}/>
                <JobsStatus status={"Failed"}/>
                <ManualJobTriggers/>
            </section>
            </span>
        </main>
    )
}