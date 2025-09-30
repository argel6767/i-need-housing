import { useRef, useCallback } from "react";

export function useDialog<T extends HTMLDialogElement = HTMLDialogElement>() {
    const dialogRef = useRef<T>(null);

    const open = useCallback(() => {
        if (dialogRef.current) {
            dialogRef.current.showModal();
        }
    }, []);

    const close = useCallback(() => {
        if (dialogRef.current) {
            dialogRef.current.close();
        }
    }, []);

    return { dialogRef, open, close };
}