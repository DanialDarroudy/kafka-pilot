package at.uibk.dps.coordinator.module.configuration.consumer.apply.business;

import at.uibk.dps.coordinator.module.configuration.consumer.apply.abstraction.IConsumerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.consumer.apply.dto.ApplyConsumerConfigRequestDto;
import at.uibk.dps.coordinator.module.configuration.consumer.config.ConsumerConfig;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConsumerConfigApplier implements IConsumerConfigApplier {
    private final ConsumerConfig config;
    private final RestTemplate restTemplate;
    private final Logger logger;

    @Override
    public void apply(String consumerId, ApplyConsumerConfigRequestDto dto) {
        var optionalBaseUrl = findBaseUrlByConsumerId(consumerId);
        if (optionalBaseUrl.isEmpty()){
            logger.logRecordBuilder()
                    .setAttribute("Message", "Consumer Id is not in the config")
                    .setAttribute("ConsumerId", consumerId)
                    .setSeverity(Severity.ERROR)
                    .emit();
            return;
        }
        var baseUrl = optionalBaseUrl.get();
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

    private Optional<String> findBaseUrlByConsumerId(String consumerId) {
        for (var instance : config.getInstances()){
            if (instance.getPromLabel().equals(consumerId)){
                return Optional.ofNullable(instance.getBaseUrl());
            }
        }
        return Optional.empty();
    }
}
