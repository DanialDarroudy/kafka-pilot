package at.uibk.dps.coordinator.module.consumer.configuration.apply.business;

import at.uibk.dps.coordinator.module.consumer.configuration.apply.abstraction.IConsumerConfigApplier;
import at.uibk.dps.coordinator.module.consumer.configuration.apply.dto.ApplyConsumerConfigRequestDto;
import at.uibk.dps.coordinator.module.consumer.configuration.config.ConsumerConfig;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ConsumerConfigApplier implements IConsumerConfigApplier {
    private final ConsumerConfig config;
    private final RestTemplate restTemplate;
    private final Logger logger;

    @Override
    public void apply(String baseUrl, ApplyConsumerConfigRequestDto dto) {
        var finalUrl = baseUrl + config.getPolicyApi();
        try {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Applying consumer config")
                    .setAttribute("RequestUrl", finalUrl)
                    .setSeverity(Severity.INFO)
                    .setBody(dto.toString())
                    .emit();

            var response = restTemplate.postForEntity(finalUrl, dto, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.logRecordBuilder()
                        .setAttribute("Message", "Successfully applied consumer config")
                        .setAttribute("RequestUrl", finalUrl)
                        .setSeverity(Severity.INFO)
                        .emit();
            } else {
                logger.logRecordBuilder()
                        .setAttribute("Message", "Failed to apply consumer config")
                        .setAttribute("RequestUrl", finalUrl)
                        .setAttribute("StatusCode", response.getStatusCode().toString())
                        .setSeverity(Severity.WARN)
                        .emit();
            }
        } catch (RestClientException exception) {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Error applying consumer config")
                    .setAttribute("RequestUrl", finalUrl)
                    .setSeverity(Severity.ERROR)
                    .setBody(exception.getMessage())
                    .emit();
        }
    }
}
