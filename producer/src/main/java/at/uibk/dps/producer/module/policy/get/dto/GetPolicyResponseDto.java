package at.uibk.dps.producer.module.policy.get.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.record.CompressionType;

@Data
@Builder
public class GetPolicyResponseDto {
    private int batchSize;
    private int lingerMs;
    private long bufferMemory;
    private CompressionType compressionType;
}
