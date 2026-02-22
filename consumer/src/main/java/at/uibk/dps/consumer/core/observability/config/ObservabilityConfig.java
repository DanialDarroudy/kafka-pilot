package at.uibk.dps.consumer.core.observability.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "observability")
public class ObservabilityConfig {
    private Log log = new Log();
    private Metric metric = new Metric();
    private Otel otel = new Otel();

    @Data
    public static class Log {
        private int exportTimeoutSeconds = 10;
        private int maxBatchSize = 512;
        private int maxQueueSize = 2048;
    }

    @Data
    public static class Metric {
        private int readIntervalSeconds = 15;
        private int exportTimeoutSeconds = 10;
    }

    @Data
    public static class Otel {
        private String collectorGrpcEndpoint;
        private int connectionTimeoutSeconds= 5;
    }
}
