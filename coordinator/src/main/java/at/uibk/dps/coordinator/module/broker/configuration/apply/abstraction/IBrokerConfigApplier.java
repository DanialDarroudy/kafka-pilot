package at.uibk.dps.coordinator.module.broker.configuration.apply.abstraction;

import at.uibk.dps.coordinator.module.broker.configuration.apply.dto.ApplyBrokerConfigRequestDto;


public interface IBrokerConfigApplier {
    void apply(String brokerId, ApplyBrokerConfigRequestDto dto);
}