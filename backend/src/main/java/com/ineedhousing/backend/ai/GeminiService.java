package com.ineedhousing.backend.ai;

import com.google.genai.Client;
import com.ineedhousing.backend.ai.models.AiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeminiService {

    private final Client client;

    public GeminiService(Client client) {
        this.client = client;
    }

    public String sendChat(String systemPrompt, String message) {
        AiMessage aiMessage = new AiMessage(systemPrompt, message);
       return client
               .models
               .generateContent("gemini-2.5-flash-lite", aiMessage.toString(), null)
               .text();
    }
}
