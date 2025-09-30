import DOMPurify from "dompurify";

interface HtmlRendererProps {
    content: string;
}

export const HtmlRenderer = ({content}: HtmlRendererProps) => {
    const sanitizedContent = DOMPurify.santitize(content);
    return (
        <div className={"w-full rounded-lg shadow-lg"} dangerouslySetInnerHTML={{ __html: sanitizedContent }}>
            {sanitizedContent}
        </div>
    )
}