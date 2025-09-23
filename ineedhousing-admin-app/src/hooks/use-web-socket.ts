// hooks/useWebSocket.ts
import { useEffect, useRef, useState, useCallback } from "react";
import { LogEvent } from "@/lib/models";

type Options = {
    reconnect?: boolean;
    onOpen?: () => void;
    onClose?: () => void;
};

export function useWebSocket(
    serviceEndpoint: string,
    { reconnect = true, onOpen, onClose }: Options = {}
) {
    const [messages, setMessages] = useState<LogEvent[]>([]);
    const wsRef = useRef<WebSocket | null>(null);
    const reconnectTimeout = useRef<NodeJS.Timeout | null>(null);
    const WEBSOCKET_URL = process.env.NEXT_PUBLIC_WEBSOCKET_URL!;
    const [isConnected, setIsConnected] = useState(false);

    const connect = useCallback(() => {
        // If already connected, skip
        if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
            return;
        }

        const ws = new WebSocket(WEBSOCKET_URL + serviceEndpoint);
        wsRef.current = ws;

        ws.onopen = () => {
            setIsConnected(true);
            onOpen?.();
            console.log("✅ Connected to WebSocket");
        };

        ws.onmessage = (event) => {
            try {
                const parsed = JSON.parse(event.data) as LogEvent;
                setMessages((prev) => [parsed, ...prev]);
            } catch {
                // fallback: wrap raw text, approximate timestamp
                setMessages((prev) => [
                    ...prev,
                    {
                        log: String(event.data),
                        level: "INFO",
                        timeStamp: new Date().toISOString(),
                        service: serviceEndpoint,
                    },
                ]);
            }
        };

        ws.onclose = () => {
            setIsConnected(false);
            console.log("❌ WebSocket Disconnected");
            onClose?.();

            if (reconnect) {
                reconnectTimeout.current = setTimeout(connect, 3000);
            }
        };

        ws.onerror = (err) => {
            console.error("⚠️ WebSocket error", err);
            ws.close();
        };
    }, [serviceEndpoint, onOpen, onClose, reconnect]);

    const close = useCallback(() => {
        if (reconnectTimeout.current) {
            clearTimeout(reconnectTimeout.current);
        }
        reconnectTimeout.current = null;
        wsRef.current?.close();
        wsRef.current = null;
        setIsConnected(false);
        setMessages([])
    }, []);

    // Cleanup when component unmounts
    useEffect(() => {
        return () => {
            if (reconnectTimeout.current) clearTimeout(reconnectTimeout.current);
            wsRef.current?.close();
        };
    }, []);

    return { messages, connect, close, isConnected };
}