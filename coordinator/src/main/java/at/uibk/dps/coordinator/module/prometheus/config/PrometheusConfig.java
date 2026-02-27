package at.uibk.dps.coordinator.module.prometheus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "prometheus")
public class PrometheusConfig {
    private String url;
}
