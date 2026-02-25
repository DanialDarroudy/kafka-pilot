package at.uibk.dps.coordinator.module.regime.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "regime-detection")
public class RegimeDetectionConfig {
    private Map<String, DetectionConfig> consumer = new HashMap<>();
    private Map<String, DetectionConfig> producer = new HashMap<>();
    private Map<String, DetectionConfig> broker = new HashMap<>();

    @Data
    public static class DetectionConfig {
        private DetectionType type;
        private Double ewmaAlpha;
        private Double cusumThreshold;
        private Double cusumDrift;
    }
}
