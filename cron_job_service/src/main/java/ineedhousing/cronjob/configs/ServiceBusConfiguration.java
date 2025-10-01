package ineedhousing.cronjob.configs;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;
import ineedhousing.cronjob.exception.exceptions.MissingConfigurationValueException;
import io.quarkus.runtime.Shutdown;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

import org.eclipse.microprofile.config.Config;

@ApplicationScoped
public class ServiceBusConfiguration {

    private final String queueName = "new-listings-job-queue";
    private String connectionString;

    @Inject
    Config config;

    @PostConstruct
    void init() {
        connectionString = config.getOptionalValue("azure.service.bus.connection-string", String.class)
                .orElseThrow(() -> new MissingConfigurationValueException("Azure Service connection string not found"));
    }

    @Produces
    @ApplicationScoped
    public ServiceBusSenderClient senderClient() {
        return new ServiceBusClientBuilder().connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
    }

    public void disposeClient(@Disposes ServiceBusSenderClient client) {
        client.close();
    }

    @Produces
    @ApplicationScoped
    public ServiceBusAdministrationClient administrationClient() {
        return new ServiceBusAdministrationClientBuilder().connectionString(connectionString).buildClient();
    }
}
