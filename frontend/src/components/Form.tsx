
interface FormProps {
    buttonLabel:string
}

export const Form = ({buttonLabel}: FormProps) => {
    return (
    <form className="mt-5">
        <div className="space-y-4">
            <div>
            <label className="text-base font-medium text-gray-900">
                Email Address
            </label>
            <div className="mt-2">
                <input
                placeholder="Email"
                type="email"
                className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                name="email"
                />
            </div>
            </div>
            <div>
            <div className="flex items-center justify-between">
                <label className="text-base font-medium text-gray-900">
                Password
                </label>
            </div>
            <div className="mt-2">
                <input
                placeholder="Password"
                type="password"
                className="flex h-10 w-full rounded-md border border-gray-300 bg-transparent px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-1 focus:ring-gray-400 focus:ring-offset-1 disabled:cursor-not-allowed disabled:opacity-50"
                name="password"
                />
            </div>
            </div>
            <div>
            <button
                className="inline-flex w-full items-center justify-center rounded-md bg-primary hover:bg-[#457F9F] px-3.5 py-2.5 font-semibold leading-7 text-white"
                type="button"
            >
                {buttonLabel}
            </button>
            </div>
        </div>
    </form>
    )
}