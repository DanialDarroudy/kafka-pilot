package at.uibk.dps.consumer.module.policy.get.abstraction;

import at.uibk.dps.consumer.module.policy.get.dto.GetPolicyResponseDto;

public interface IPolicyGetter {
    GetPolicyResponseDto getPolicy();
}
