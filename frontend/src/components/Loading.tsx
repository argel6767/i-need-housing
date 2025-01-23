
interface loadingProps {
    loadingMessage?: string
}

export const Loading = ({loadingMessage}: loadingProps) => {
    return (
        <main className="flex flex-col items-center">
            <span className=" flex-1 loading loading-spinner w-full items-center"></span>
            <h1 className="text-2xl">{loadingMessage}</h1>
        </main>
        
    )
}