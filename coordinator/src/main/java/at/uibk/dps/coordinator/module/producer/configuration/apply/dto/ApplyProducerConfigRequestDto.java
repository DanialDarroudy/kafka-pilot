package at.uibk.dps.coordinator.module.producer.configuration.apply.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.record.CompressionType;

@Data
@Builder
public class ApplyProducerConfigRequestDto {
    private int batchSize;
    private int lingerMs;
    private long bufferMemory;
    private CompressionType compressionType;
    private String acks;
}
