package at.uibk.dps.consumer.core.config;

import lombok.Data;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
    private String bootstrapServers;

    public Properties getProperties() {
        var properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("fetch.min.bytes", fetchMinBytes);
        properties.put("fetch.max.wait.ms", fetchMaxWaitMs);
        properties.put("max.poll.records", maxPollRecords);
        properties.put("key.deserializer", StringSerializer.class.getName());
        properties.put("value.deserializer", ByteArrayDeserializer.class.getName());
        return properties;
    }
}
