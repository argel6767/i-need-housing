package ineedhousing.cronjob.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;


@ApplicationScoped
public class GCPServiceAccessKeyDecoder {

    @Inject
    Config config;

    private String encodedServiceAccessKey;

    @PostConstruct
    public void init() {
        encodedServiceAccessKey = config.getOptionalValue("encoded.service.access.key", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Encode Service Access Key not present!"));
    }

    public String getServiceAccessKey() throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedServiceAccessKey);
        String serviceKeyJson = new String(decodedBytes, StandardCharsets.UTF_8);

        GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(new ByteArrayInputStream(serviceKeyJson.getBytes()))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
        credentials.refresh();
        return credentials.getAccessToken().getTokenValue();
    }
}
