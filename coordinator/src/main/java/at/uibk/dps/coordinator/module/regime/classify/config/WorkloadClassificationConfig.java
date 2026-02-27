package at.uibk.dps.coordinator.module.regime.classify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "workload-classification")
public class WorkloadClassificationConfig {
    private Map<String, Map<String, Condition>> metrics = new HashMap<>();

    @Data
    public static class Condition {
        private String operator;
        private Double threshold;
    }
}
