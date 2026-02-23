package at.uibk.dps.coordinator.module.config.producer.apply.business;

import at.uibk.dps.coordinator.core.config.CoordinatorConfig;
import at.uibk.dps.coordinator.module.config.producer.apply.abstraction.IProducerConfigApplier;
import at.uibk.dps.coordinator.module.config.producer.apply.dto.ApplyProducerConfigRequestDto;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProducerConfigApplier implements IProducerConfigApplier {
    private final CoordinatorConfig config;
    private final RestTemplate restTemplate;
    private final Logger logger;

    @Override
    public void apply(String baseUrl, ApplyProducerConfigRequestDto dto) {
        var finalUrl = baseUrl + config.getProducer().getPolicyApi();
        try {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Applying producer config")
                    .setAttribute("RequestUrl", finalUrl)
                    .setSeverity(Severity.INFO)
                    .setBody(dto.toString())
                    .emit();

            var response = restTemplate.postForEntity(finalUrl, dto, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.logRecordBuilder()
                        .setAttribute("Message", "Successfully applied producer config")
                        .setAttribute("RequestUrl", finalUrl)
                        .setSeverity(Severity.INFO)
                        .emit();
            } else {
                logger.logRecordBuilder()
                        .setAttribute("Message", "Failed to apply producer config")
                        .setAttribute("RequestUrl", finalUrl)
                        .setAttribute("StatusCode", response.getStatusCode().toString())
                        .setSeverity(Severity.WARN)
                        .emit();
            }
        } catch (RestClientException exception) {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Error applying producer config")
                    .setAttribute("RequestUrl", finalUrl)
                    .setSeverity(Severity.ERROR)
                    .setBody(exception.getMessage())
                    .emit();
        }
    }
}
