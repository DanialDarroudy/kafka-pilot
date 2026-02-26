package at.uibk.dps.consumer.module.policy.update.business;

import at.uibk.dps.consumer.core.config.ConsumerConfig;
import at.uibk.dps.consumer.module.consumer.manage.abstraction.IConsumerManager;
import at.uibk.dps.consumer.module.policy.update.abstraction.IPolicyUpdater;
import at.uibk.dps.consumer.module.policy.update.dto.UpdatePolicyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyUpdater implements IPolicyUpdater {
    private final ConsumerConfig config;
    private final IConsumerManager consumerManager;

    @Override
    public void updatePolicy(UpdatePolicyRequestDto dto) {
        config.setFetchMinBytes(dto.getFetchMinBytes());
        config.setFetchMaxWaitMs(dto.getFetchMaxWaitMs());
        config.setMaxPollRecords(dto.getMaxPollRecords());
        consumerManager.policyChanged();
    }
}
