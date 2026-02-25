package at.uibk.dps.coordinator.module.consumer.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "consumer")
public class ConsumerConfig {
    private String policyApi;
    private Instance[] instances;

    @Data
    public static class Instance{
        private String baseUrl;
        private String promLabel;
    }
}
