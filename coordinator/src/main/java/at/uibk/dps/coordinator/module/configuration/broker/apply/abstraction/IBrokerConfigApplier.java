package at.uibk.dps.coordinator.module.configuration.broker.apply.abstraction;

import at.uibk.dps.coordinator.module.configuration.broker.apply.dto.ApplyBrokerConfigRequestDto;


public interface IBrokerConfigApplier {
    void apply(String brokerId, ApplyBrokerConfigRequestDto dto);
}