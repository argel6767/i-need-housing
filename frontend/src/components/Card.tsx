import Image, { StaticImageData } from "next/image"
import { ReactNode } from "react"

interface CardProps {
    image: StaticImageData,
    alt: string
    body?: string
    children?:ReactNode
}

export const Card = ({image, alt, children, body}: CardProps) => {
    return (
        <div className="carousel-item hover:scale-105 transition-transform duration-300  bg-slate-100 rounded-box">
            <div className="card bg-base-200 shadow-xl">
                <div className="card-body text-center">
                    <p className="font-bold">{body}</p>
                    {children}
                </div>
                <figure className="">
                    <Image src={image} alt={alt} width={274} height={275} className="w-full"/>
                </figure>

            </div>
        </div>
    )
}