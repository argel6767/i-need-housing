package com.ineedhousing.new_listings_service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Component
public class ApiTokenGenerator {


    @Value("${token.generation.secret.key}")
    private String secretKey;

    public String generateApiToken(String serviceName) {
        String tokenInfo = String.format("%s:%s", serviceName, Instant.now().toEpochMilli());
        String encryptedInfo = encryptTokenInfo(tokenInfo);

        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // Create HMAC signature to prevent tampering
        String signature = createSignature(serviceName, randomPart);

        return String.format("svc_%s_%s_%s", encryptedInfo, randomPart, signature);
    }


    private String encryptTokenInfo(String tokenInfo) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.substring(0, 16).getBytes(StandardCharsets.UTF_8), "AES");

            // Generate random IV
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(tokenInfo.getBytes(StandardCharsets.UTF_8));

            // Combine IV + encrypted data
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt token info", e);
        }
    }

    private String createSignature(String serviceName, String randomPart) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);

            String data = serviceName + ":" + randomPart;
            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature).substring(0, 16);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create token signature", e);
        }
    }
}
