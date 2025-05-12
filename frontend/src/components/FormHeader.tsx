import Link from "next/link"

interface FormHeaderProps {
    header: string,
    text: string
    buttonLabel: string
    path: string
}

export const FormHeader = ({header, text, buttonLabel, path}: FormHeaderProps) => {
    return (
        <main className="py-3">
           <div className="mb-2 flex"></div>
                <h2 className="text-2xl font-bold leading-tight">{header}</h2>
                <p className="mt-2 text-base text-gray-600 font-medium">{text}<Link className="hover:underline hover:font-semibold" href={path}>{buttonLabel}</Link></p>
        </main>
    )
}