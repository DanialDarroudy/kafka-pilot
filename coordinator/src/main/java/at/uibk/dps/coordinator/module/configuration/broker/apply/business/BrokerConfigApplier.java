package at.uibk.dps.coordinator.module.configuration.broker.apply.business;

import at.uibk.dps.coordinator.module.configuration.broker.apply.abstraction.IBrokerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.broker.apply.dto.ApplyBrokerConfigRequestDto;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AlterConfigOp.OpType.SET;

@Component
@RequiredArgsConstructor
public class BrokerConfigApplier implements IBrokerConfigApplier {
    private final AdminClient adminClient;
    private final Logger logger;

    @Override
    public void apply(String brokerId, ApplyBrokerConfigRequestDto dto) {
        try {
            var id = brokerId.replace("broker-", "");
            var resource = new ConfigResource(ConfigResource.Type.BROKER, id);
            var ops = dto.toKafkaConfigMap().entrySet().stream()
                    .map(e -> new AlterConfigOp(new ConfigEntry(e.getKey(), e.getValue()), SET)).toList();

            adminClient.incrementalAlterConfigs(Map.of(resource, ops)).all().get();
        } catch (Exception exception) {
            logger.logRecordBuilder()
                    .setAttribute("Message", "Error applying broker config")
                    .setAttribute("BrokerId", brokerId)
                    .setSeverity(Severity.ERROR)
                    .setBody(exception.getMessage())
                    .emit();
        }
    }
}