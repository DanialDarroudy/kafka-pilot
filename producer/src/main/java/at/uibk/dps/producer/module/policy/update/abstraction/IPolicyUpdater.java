package at.uibk.dps.producer.module.policy.update.abstraction;

import at.uibk.dps.producer.module.policy.update.dto.UpdatePolicyRequestDto;

public interface IPolicyUpdater {
    void updatePolicy(UpdatePolicyRequestDto dto);
}
