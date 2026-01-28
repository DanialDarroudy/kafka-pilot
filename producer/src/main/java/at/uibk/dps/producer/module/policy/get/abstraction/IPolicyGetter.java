package at.uibk.dps.producer.module.policy.get.abstraction;

import at.uibk.dps.producer.module.policy.get.dto.GetPolicyResponseDto;

public interface IPolicyGetter {
    GetPolicyResponseDto getPolicy();
}
