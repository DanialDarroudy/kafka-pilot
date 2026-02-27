package at.uibk.dps.coordinator.module.configuration.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "producer")
public class ProducerConfig {
    private String policyApi;
    private Instance[] instances;

    @Data
    public static class Instance{
        private String baseUrl;
        private String promLabel;
    }
}
