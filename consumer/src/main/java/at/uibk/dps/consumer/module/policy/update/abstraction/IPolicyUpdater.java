package at.uibk.dps.consumer.module.policy.update.abstraction;

import at.uibk.dps.consumer.module.policy.update.dto.UpdatePolicyRequestDto;

public interface IPolicyUpdater {
    void updatePolicy(UpdatePolicyRequestDto dto);
}
