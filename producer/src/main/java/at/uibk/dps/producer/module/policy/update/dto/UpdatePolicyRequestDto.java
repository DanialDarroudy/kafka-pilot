package at.uibk.dps.producer.module.policy.update.dto;

import lombok.Data;
import org.apache.kafka.common.record.CompressionType;

@Data
public class UpdatePolicyRequestDto {
    private int batchSize;
    private int lingerMs;
    private long bufferMemory;
    private CompressionType compressionType;
    private String acks;
}
