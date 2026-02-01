package at.uibk.dps.producer.module.message.send.business;

import at.uibk.dps.producer.module.message.send.abstraction.IMessageSender;
import at.uibk.dps.producer.module.message.send.dto.SendMessageRequestDto;
import at.uibk.dps.producer.module.producer.manage.abstraction.IProducerManager;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender implements IMessageSender {
    private final IProducerManager producerManager;

    @Override
    public void send(SendMessageRequestDto dto) {
        var producer = producerManager.getProducer();
        var record = new ProducerRecord<>(dto.getTopic(), dto.getKey(), dto.getPayload());
        try {
            producer.send(record);
        } catch (KafkaException e) {
            // for race condition on reconfiguration
            producerManager.getProducer().send(record);
        }
    }
}
