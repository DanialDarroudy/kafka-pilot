package at.uibk.dps.consumer.module.policy.update.dto;

import lombok.Data;

@Data
public class UpdatePolicyRequestDto {
    private long fetchMinBytes;
    private int fetchMaxWaitMs;
    private long maxPollRecords;
}
