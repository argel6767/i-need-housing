import DOMPurify from "dompurify";

interface HtmlRendererProps {
    content: string;
}

export const HtmlRenderer = ({ content }: HtmlRendererProps) => {
    const sanitizedContent = DOMPurify.sanitize(content);
    return (
        <div
            className="w-full p-2"
            dangerouslySetInnerHTML={{ __html: sanitizedContent }}
        />
    );
};