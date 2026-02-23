package at.uibk.dps.coordinator.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coordinator")
public class CoordinatorConfig {
    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();
    private Broker broker = new Broker();

    @Data
    public static class Producer {
        private String policyApi;
        private String[] baseUrls;
    }

    @Data
    public static class Consumer {
        private String policyApi;
        private String[] baseUrls;
    }

    @Data
    public static class Broker {
        private String bootstrapServers;
        private int[] ids;
    }
}
