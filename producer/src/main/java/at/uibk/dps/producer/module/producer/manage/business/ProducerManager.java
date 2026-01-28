package at.uibk.dps.producer.module.producer.manage.business;

import at.uibk.dps.producer.core.config.ProducerConfig;
import at.uibk.dps.producer.module.producer.manage.abstraction.IProducerManager;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ProducerManager implements IProducerManager {
    private final Logger logger;
    private final ProducerConfig config;
    private final AtomicReference<KafkaProducer<String, byte[]>> producerAtomicReference;

    public ProducerManager(Logger logger, ProducerConfig config) {
        this.logger = logger;
        this.config = config;
        this.producerAtomicReference = new AtomicReference<>(new KafkaProducer<>(config.getProperties()));
    }

    @Override
    public synchronized void policyChanged() {
        var oldProducer = this.producerAtomicReference.get();

        logger.logRecordBuilder()
                .setAttribute("Message", "create and set new producer due to policy change")
                .setSeverity(Severity.INFO);

        this.producerAtomicReference.set(new KafkaProducer<>(config.getProperties()));

        oldProducer.flush();
        oldProducer.close();

        logger.logRecordBuilder()
                .setAttribute("Message", "flushed and closed old producer due to policy change")
                .setSeverity(Severity.INFO);
    }

    @Override
    public KafkaProducer<String, byte[]> getProducer() {
        return this.producerAtomicReference.get();
    }
}
