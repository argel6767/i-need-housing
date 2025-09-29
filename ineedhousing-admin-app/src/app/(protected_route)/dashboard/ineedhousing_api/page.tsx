import {Navbar} from "@/components/navbar";
import {LogStreamViewer, PingService} from "@/components/service-functions";

export default function INeedHousingAPIPage() {
    return (
        <main className="h-screen overflow-clip">
            <Navbar />
            <h1 className={"text-5xl  text-center py-4"}>INeedHousing Backend API</h1>
            <span className={"flex h-full w-full p-4"}>
                <section className={"w-4/6"}>
                <LogStreamViewer serviceEndpoint={"/logs"} service={"INeedHousing API"} />
            </section>
            <section className={"w-2/6 flex flex-col items-center gap-5"}>
                <PingService service={"Keymaster Service"}/>
            </section>
            </span>
        </main>
    )
}