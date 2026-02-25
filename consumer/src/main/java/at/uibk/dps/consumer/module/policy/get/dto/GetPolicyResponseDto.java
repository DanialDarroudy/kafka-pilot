package at.uibk.dps.consumer.module.policy.get.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPolicyResponseDto {
    private int fetchMinBytes;
    private int fetchMaxWaitMs;
    private int maxPollRecords;
    private int maxPollIntervalMs;
    private int sessionTimeoutMs;
}
