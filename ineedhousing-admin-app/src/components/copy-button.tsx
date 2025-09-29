'use client'

import {useState} from "react";

interface CopyButtonProps {
    value: string
}

export const CopyButton = ({ value }: CopyButtonProps) => {
    const [copied, setCopied] = useState(false);

    const handleCopy = async () => {
        if (!value) return;
        try {
            await navigator.clipboard.writeText(value);
            setCopied(true);
            setTimeout(() => setCopied(false), 1500);
        } catch (e) {
            alert("Failed to copy!");
        }
    };

    return (
        <button
            type="button"
            className="ml-2 btn btn-sm btn-outline"
            onClick={handleCopy}
            tabIndex={-1}
        >
            {copied ? "Copied!" : "Copy"}
        </button>
    );
}