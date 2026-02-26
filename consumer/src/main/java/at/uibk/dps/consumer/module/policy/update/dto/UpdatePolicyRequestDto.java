package at.uibk.dps.consumer.module.policy.update.dto;

import lombok.Data;

@Data
public class UpdatePolicyRequestDto {
    private int fetchMinBytes;
    private int fetchMaxWaitMs;
    private int maxPollRecords;
}
