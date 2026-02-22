package at.uibk.dps.consumer.core.config;

import lombok.Data;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "consumer")
public class ConsumerConfig {
    private long fetchMinBytes = 1048576;
    private int fetchMaxWaitMs = 50;
    private long maxPollRecords = 500;
    private int maxPollIntervalMs = 300000;
    private int sessionTimeoutMs = 10000;
    private String bootstrapServers;
    private String topic = "messages";
    private String groupId = "consumer-group";

    public Properties getProperties() {
        var properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("fetch.min.bytes", fetchMinBytes);
        properties.put("fetch.max.wait.ms", fetchMaxWaitMs);
        properties.put("max.poll.records", maxPollRecords);
        properties.put("session.timeout.ms", sessionTimeoutMs);
        properties.put("max.poll.interval.ms", maxPollIntervalMs);
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", ByteArrayDeserializer.class.getName());
        return properties;
    }
}
