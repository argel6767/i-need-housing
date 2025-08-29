package com.ineedhousing.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@ApplicationScoped
public class TokenHasher {

    @Inject
    Config config;

    private String secretKey;

    @jakarta.annotation.PostConstruct
    public void init() {
        secretKey = config.getOptionalValue("api.token.secret.key", String.class)
                .orElseThrow(() -> new IllegalStateException("Secret Key is not present"));
    }

    public String hashToken(String token) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }

    public boolean matches(String token, String storedHash) {
        String tokenHash = hashToken(token);
        // Use constant-time comparison to prevent timing attacks
        return MessageDigest.isEqual(tokenHash.getBytes(StandardCharsets.UTF_8), storedHash.getBytes(StandardCharsets.UTF_8));
    }
}