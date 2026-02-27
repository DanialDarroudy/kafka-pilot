package at.uibk.dps.coordinator.module.prometheus.query.business;

import at.uibk.dps.coordinator.module.prometheus.config.PrometheusConfig;
import at.uibk.dps.coordinator.module.prometheus.query.abstraction.IPrometheusClient;
import at.uibk.dps.coordinator.module.prometheus.query.dto.PrometheusResponseDto;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrometheusClient implements IPrometheusClient {
    private final RestTemplate restTemplate;
    private final PrometheusConfig config;
    private final Logger logger;

    @Override
    public double query(String promQl) {
        try {
            var encoded = URLEncoder.encode(promQl, StandardCharsets.UTF_8);
            var url = config.getUrl() + "/api/v1/query?query=" + encoded;
            var response = restTemplate.getForObject(url, PrometheusResponseDto.class);
            if (response == null) {
                return 0.0;
            }
            if (!"success".equalsIgnoreCase(response.getStatus())) {
                logger.logRecordBuilder()
                        .setAttribute("Message", "Prometheus query failed")
                        .setAttribute("RequestUrl", url)
                        .setAttribute("Status", response.getStatus())
                        .setSeverity(Severity.ERROR)
                        .emit();
                return 0.0;
            }
            if (response.getData() == null || response.getData().getResult() == null || response.getData().getResult().isEmpty()) {
                return 0.0;
            }
            return Optional.of(response.getData().getResult().get(0))
                    .map(PrometheusResponseDto.ResultNode::getMetricValue)
                    .orElse(0.0);

        } catch (Exception exception) {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Error querying Prometheus")
                    .setAttribute("PromQl", promQl)
                    .setSeverity(Severity.ERROR)
                    .setBody(exception.getMessage())
                    .emit();
            return 0.0;
        }
    }
}
