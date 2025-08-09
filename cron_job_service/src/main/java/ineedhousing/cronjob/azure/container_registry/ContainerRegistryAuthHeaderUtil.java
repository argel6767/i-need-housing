package ineedhousing.cronjob.azure.container_registry;

import java.util.Base64;

public class ContainerRegistryAuthHeaderUtil {
    public static String createBasicAuthHeader(String username, String accessKey) {
        String credentials = username + ":" + accessKey;
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
