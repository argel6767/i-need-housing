
export interface ModalProps {
    children?: React.ReactNode,
    onClick?: () => void,
}

export const Modal = ({ children, onClick }: ModalProps) => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 animate-fade"
             onClick={onClick}>
            <div
                className="relative p-6 rounded-xl shadow-lg flex flex-col gap-5 bg-slate-200 w-11/12 md:w-3/4 lg:w-2/5 max-h-[100vh] justify-center overflow-y-auto"
                onClick={(e) => e.stopPropagation()}>
                {children}
            </div>
        </div>
    )
}