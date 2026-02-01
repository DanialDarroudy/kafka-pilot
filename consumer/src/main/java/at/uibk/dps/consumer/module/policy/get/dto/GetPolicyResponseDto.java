package at.uibk.dps.consumer.module.policy.get.dto;

import lombok.Data;

@Data
public class GetPolicyResponseDto {
    private long fetchMinBytes;
    private int fetchMaxWaitMs;
    private long maxPollRecords;
}
