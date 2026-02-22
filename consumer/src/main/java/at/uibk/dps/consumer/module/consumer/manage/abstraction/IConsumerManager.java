package at.uibk.dps.consumer.module.consumer.manage.abstraction;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface IConsumerManager {
    void policyChanged();
    KafkaConsumer<String, byte[]> getConsumer();
}
