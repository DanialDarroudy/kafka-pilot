package at.uibk.dps.coordinator.module.configuration.consumer.apply.abstraction;


import at.uibk.dps.coordinator.module.configuration.consumer.apply.dto.ApplyConsumerConfigRequestDto;

public interface IConsumerConfigApplier {
    void apply(String consumerId, ApplyConsumerConfigRequestDto dto);
}
