import Link from "next/link";
import React from "react";
import {ChevronLeft} from "lucide-react";

interface BackButtonProps {
    backPath: string;
}
export const BackButton = ({ backPath }: BackButtonProps) => {
    return (
        <Link href={backPath}
              className="btn btn-outline hover:btn-primary text-xl md:text-2xl lg:text-3xl text-primary font-bold"><ChevronLeft/> Go Back</Link>
    )
}