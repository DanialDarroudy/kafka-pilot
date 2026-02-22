package at.uibk.dps.consumer.module.message.poll.business;

import at.uibk.dps.consumer.core.config.ConsumerConfig;
import at.uibk.dps.consumer.module.message.poll.abstraction.IMessagePoller;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class MessagePoller implements IMessagePoller {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private KafkaConsumer<String, byte[]> consumer;
    private Thread pollThread;
    private ConsumerConfig config;

    @Override
    public synchronized void start(KafkaConsumer<String, byte[]> consumer) {
        this.consumer = consumer;
        running.set(true);
        pollThread = new Thread(this::run, "consumer-poll-thread");
        pollThread.start();
    }

    @Override
    public synchronized void stop() {
        running.set(false);
        consumer.wakeup();
        try {
            pollThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    private void run() {
        try {
            while (running.get()) {
                var records = consumer.poll(Duration.ofMillis(2L * config.getFetchMaxWaitMs()));
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
