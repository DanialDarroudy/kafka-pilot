package at.uibk.dps.coordinator.module.config.consumer.apply.dto;

import lombok.Data;

@Data
public class ApplyConsumerConfigRequestDto {
    private long fetchMinBytes;
    private int fetchMaxWaitMs;
    private long maxPollRecords;
    private int maxPollIntervalMs;
    private int sessionTimeoutMs;
}
