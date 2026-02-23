package at.uibk.dps.coordinator.module.config.producer.apply.dto;

import lombok.Data;
import org.apache.kafka.common.record.CompressionType;

@Data
public class ApplyProducerConfigRequestDto {
    private int batchSize;
    private int lingerMs;
    private long bufferMemory;
    private CompressionType compressionType;
    private String acks;
}
