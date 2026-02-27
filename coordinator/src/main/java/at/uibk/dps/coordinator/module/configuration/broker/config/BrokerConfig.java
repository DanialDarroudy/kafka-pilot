package at.uibk.dps.coordinator.module.configuration.broker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "broker")
public class BrokerConfig {
    private String bootstrapServers;
    private String[] ids;
}
