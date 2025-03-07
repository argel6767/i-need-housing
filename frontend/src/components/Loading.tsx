
interface loadingProps {
    loadingMessage?: string
}

/** default loading component */
export const Loading = ({loadingMessage}: loadingProps) => {
    return (
        <main className="flex flex-col items-center">
            <span className=" flex-1 loading loading-spinner w-full items-center"></span>
            <h1 className="text-2xl">{loadingMessage}</h1>
        </main>
        
    )


}

export const SkeletonText = () => {
    return (
        <main className="w-5/12">
             <div className="skeleton h-6 w-full bg-slate-200"></div>
        </main>
    )
}

export const Skeleton = () => {
    return (
        <main className="w-full p-2">
            <div className="flex w-52 flex-col gap-4">
            <div className="skeleton h-48 w-full bg-slate-200"></div>
            <div className="skeleton h-4 w-28 bg-slate-200"></div>
            <div className="skeleton h-4 w-full bg-slate-200"></div>
            </div>
        </main>
        
    )
}

interface GroupOfSkeletonsProps {
    numOfSkeletons: number
}

export const GroupOfSkeletons = ({numOfSkeletons}: GroupOfSkeletonsProps) => {
    return (
        <>
            {Array.from({length : numOfSkeletons}, (_, index) =>
            <Skeleton key={index}/>)}
        </>
    );
}
