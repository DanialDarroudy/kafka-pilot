package at.uibk.dps.producer.module.producer.manage.abstraction;

import org.apache.kafka.clients.producer.KafkaProducer;

public interface IProducerManager {
    void policyChanged();
    KafkaProducer<String, byte[]> getProducer();
}
