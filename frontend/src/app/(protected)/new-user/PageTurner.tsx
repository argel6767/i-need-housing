'use client'

import { ArrowBigLeft, ArrowBigRight } from "lucide-react"
import Link from "next/link"
import { useState } from "react"

interface PageTurnerProps {
    href:string
    direction: string
}

export const PageTurner = ({href, direction}:PageTurnerProps) => {

    const [isUserHovering, setIsUserHovering] = useState<boolean>(false);

    const props = {
        size: 70,
        ... isUserHovering  && { fill: '#000000' },
        onMouseEnter: () => {setIsUserHovering(true)},
        onMouseLeave: () => {setIsUserHovering(false)},
    }
    return (
        <Link href={href}>
            {direction === "left"? (<ArrowBigLeft className="hover:cursor-pointer hover:scale-110 transition-transform duration-300" {...props}/>) 
            : (<ArrowBigRight className="hover:cursor-pointer hover:scale-110 transition-transform duration-300" {...props}/>)}
        </Link>
    )
}