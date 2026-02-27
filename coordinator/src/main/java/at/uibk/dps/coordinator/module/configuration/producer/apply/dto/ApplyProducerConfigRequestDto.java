package at.uibk.dps.coordinator.module.configuration.producer.apply.dto;

import at.uibk.dps.coordinator.module.policy.config.PolicyBankConfig;
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

    public static ApplyProducerConfigRequestDto createWithPolicy(PolicyBankConfig.ProducerPolicy policy){
        return builder()
                .batchSize(policy.getBatchSize())
                .lingerMs(policy.getLingerMs())
                .bufferMemory(policy.getBufferMemory())
                .compressionType(policy.getCompressionType())
                .build();
    }
}
