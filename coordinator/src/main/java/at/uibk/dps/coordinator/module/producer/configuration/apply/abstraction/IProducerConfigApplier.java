package at.uibk.dps.coordinator.module.producer.configuration.apply.abstraction;

import at.uibk.dps.coordinator.module.producer.configuration.apply.dto.ApplyProducerConfigRequestDto;

public interface IProducerConfigApplier {
    void apply(String producerId, ApplyProducerConfigRequestDto dto);
}
