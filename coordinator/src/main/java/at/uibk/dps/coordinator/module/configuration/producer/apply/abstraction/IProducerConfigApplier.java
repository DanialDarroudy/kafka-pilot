package at.uibk.dps.coordinator.module.configuration.producer.apply.abstraction;

import at.uibk.dps.coordinator.module.configuration.producer.apply.dto.ApplyProducerConfigRequestDto;

public interface IProducerConfigApplier {
    void apply(String producerId, ApplyProducerConfigRequestDto dto);
}
