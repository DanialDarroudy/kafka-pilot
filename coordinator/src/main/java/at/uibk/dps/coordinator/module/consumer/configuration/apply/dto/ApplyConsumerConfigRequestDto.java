package at.uibk.dps.coordinator.module.consumer.configuration.apply.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyConsumerConfigRequestDto {
    private long fetchMinBytes;
    private int fetchMaxWaitMs;
    private long maxPollRecords;
    private int maxPollIntervalMs;
    private int sessionTimeoutMs;
}
