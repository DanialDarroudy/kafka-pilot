package at.uibk.dps.coordinator.module.configuration.consumer.apply.dto;

import at.uibk.dps.coordinator.module.policy.config.PolicyBankConfig;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyConsumerConfigRequestDto {
    private long fetchMinBytes;
    private int fetchMaxWaitMs;
    private long maxPollRecords;

    public static ApplyConsumerConfigRequestDto createWithPolicy(PolicyBankConfig.ConsumerPolicy policy) {
        return builder()
                .fetchMinBytes(policy.getFetchMinBytes())
                .fetchMaxWaitMs(policy.getFetchMaxWaitMs())
                .maxPollRecords(policy.getMaxPollRecords())
                .build();
    }
}
