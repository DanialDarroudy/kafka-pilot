package at.uibk.dps.producer.core.config;

import lombok.Data;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "producer")
public class ProducerConfig {
    private int batchSize = 16384;
    private int lingerMs = 1;
    private long bufferMemory = 33554432;
    private CompressionType compressionType = CompressionType.NONE;
    private String bootstrapServers;

    public Properties getProperties() {
        var properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("batch.size", batchSize);
        properties.put("linger.ms", lingerMs);
        properties.put("buffer.memory", bufferMemory);
        properties.put("compression.type", compressionType);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", ByteArrayDeserializer.class.getName());
        return properties;
    }
}
