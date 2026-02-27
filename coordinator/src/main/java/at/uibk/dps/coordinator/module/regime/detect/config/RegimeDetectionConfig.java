package at.uibk.dps.coordinator.module.regime.detect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "regime-detection")
public class RegimeDetectionConfig {
    private Map<String, DetectionConfig> metricDetectionMap = new HashMap<>();

    @Data
    public static class DetectionConfig {
        private DetectionType type;
        private Double ewmaAlpha;
        private Double cusumThreshold;
        private Double cusumDrift;
        private Double eventThreshold;
    }
}
