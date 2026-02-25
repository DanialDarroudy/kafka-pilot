package at.uibk.dps.producer.module.message.send.business;

import at.uibk.dps.producer.core.config.ProducerConfig;
import at.uibk.dps.producer.module.message.send.abstraction.IMessageSender;
import at.uibk.dps.producer.module.message.send.dto.SendMessageRequestDto;
import at.uibk.dps.producer.module.producer.manage.abstraction.IProducerManager;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender implements IMessageSender {
    private final IProducerManager producerManager;
    private final ProducerConfig config;
    private final Logger logger;

    @Override
    public void send(SendMessageRequestDto dto) {
        var producer = producerManager.getProducer();
        var record = new ProducerRecord<>(config.getTopic(), dto.getKey(), dto.getPayload());
        try {
            producer.send(record);
        } catch (KafkaException exception) {
            // for race condition on reconfiguration
            var currentProducer = producerManager.getProducer();
            if (currentProducer != producer) {
                // Producer was swapped due to policy change
                currentProducer.send(record);
            } else {
                // real error, not reconfiguration
                logger.logRecordBuilder()
                        .setAttribute("Message", "Error during sending message")
                        .setSeverity(Severity.ERROR)
                        .setBody(exception.getMessage())
                        .emit();
            }
        }
    }
}
