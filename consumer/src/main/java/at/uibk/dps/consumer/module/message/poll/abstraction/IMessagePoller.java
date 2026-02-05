package at.uibk.dps.consumer.module.message.poll.abstraction;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface IMessagePoller {
    void start(KafkaConsumer<String, byte[]> consumer);
    void stop();
}
