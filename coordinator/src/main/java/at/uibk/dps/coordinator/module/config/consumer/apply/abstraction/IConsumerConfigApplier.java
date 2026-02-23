package at.uibk.dps.coordinator.module.config.consumer.apply.abstraction;


import at.uibk.dps.coordinator.module.config.consumer.apply.dto.ApplyConsumerConfigRequestDto;

public interface IConsumerConfigApplier {
    void apply(String baseUrl, ApplyConsumerConfigRequestDto dto);
}
