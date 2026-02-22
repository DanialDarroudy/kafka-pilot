package at.uibk.dps.consumer.module.consumer.manage.business;

import at.uibk.dps.consumer.core.config.ConsumerConfig;
import at.uibk.dps.consumer.module.consumer.manage.abstraction.IConsumerManager;
import at.uibk.dps.consumer.module.message.poll.abstraction.IMessagePoller;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ConsumerManager implements IConsumerManager {
    private final Logger logger;
    private final ConsumerConfig config;
    private final IMessagePoller messagePoller;
    private AtomicReference<KafkaConsumer<String, byte[]>> consumerAtomicReference;

    public ConsumerManager(Logger logger, ConsumerConfig config, IMessagePoller messagePoller) {
        this.logger = logger;
        this.config = config;
        this.messagePoller = messagePoller;
        startConsumer();
    }

    @Override
    public synchronized void policyChanged() {
        logger.logRecordBuilder()
                .setAttribute("Message", "consumer policy changed, restarting consumer")
                .setSeverity(Severity.INFO);

        stopConsumer();
        startConsumer();

        logger.logRecordBuilder()
                .setAttribute("Message", "consumer restarted with new policy")
                .setSeverity(Severity.INFO);
    }

    @Override
    public KafkaConsumer<String, byte[]> getConsumer() {
        return consumerAtomicReference.get();
    }

    private void startConsumer() {
        var consumer = new KafkaConsumer<String, byte[]>(config.getProperties());
        consumerAtomicReference.set(consumer);
        consumer.subscribe(Collections.singletonList(config.getTopic()));
        messagePoller.start(consumer);
    }

    private void stopConsumer() {
        messagePoller.stop();
    }
}
