import svg from "../../public/file.svg"
import Image from "next/image";

export default function Home() {
  return (
    <main className={"bg-foreground h-screen p-4"}>
      <h1 className={"flex justify-center text-6xl text-primary font-bold"}>INeedHousing <Image src={svg} alt={"INeedHousing Logo"} width={50} height={50}/></h1>
    </main>
  );
}
