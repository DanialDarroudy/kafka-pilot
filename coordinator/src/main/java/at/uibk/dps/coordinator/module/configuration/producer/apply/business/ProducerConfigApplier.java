package at.uibk.dps.coordinator.module.configuration.producer.apply.business;

import at.uibk.dps.coordinator.module.configuration.producer.apply.abstraction.IProducerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.producer.apply.dto.ApplyProducerConfigRequestDto;
import at.uibk.dps.coordinator.module.configuration.producer.config.ProducerConfig;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProducerConfigApplier implements IProducerConfigApplier {
    private final ProducerConfig config;
    private final RestTemplate restTemplate;
    private final Logger logger;

    @Override
    public void apply(String producerId, ApplyProducerConfigRequestDto dto) {
        var optionalBaseUrl = findBaseUrlByProducerId(producerId);
        if (optionalBaseUrl.isEmpty()){
            logger.logRecordBuilder()
                    .setAttribute("Message", "Producer Id is not in the config")
                    .setAttribute("ProducerId", producerId)
                    .setSeverity(Severity.ERROR)
                    .emit();
            return;
        }
        var baseUrl = optionalBaseUrl.get();
        var finalUrl = baseUrl + config.getPolicyApi();
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

    private Optional<String> findBaseUrlByProducerId(String producerId) {
        for (var instance : config.getInstances()){
            if (instance.getPromLabel().equals(producerId)){
                return Optional.ofNullable(instance.getBaseUrl());
            }
        }
        return Optional.empty();
    }
}
