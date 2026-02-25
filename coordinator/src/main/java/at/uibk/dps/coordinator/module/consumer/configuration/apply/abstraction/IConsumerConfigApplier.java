package at.uibk.dps.coordinator.module.consumer.configuration.apply.abstraction;


import at.uibk.dps.coordinator.module.consumer.configuration.apply.dto.ApplyConsumerConfigRequestDto;

public interface IConsumerConfigApplier {
    void apply(String baseUrl, ApplyConsumerConfigRequestDto dto);
}
