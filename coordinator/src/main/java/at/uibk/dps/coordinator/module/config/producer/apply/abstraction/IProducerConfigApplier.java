package at.uibk.dps.coordinator.module.config.producer.apply.abstraction;

import at.uibk.dps.coordinator.module.config.producer.apply.dto.ApplyProducerConfigRequestDto;

public interface IProducerConfigApplier {
    void apply(String baseUrl, ApplyProducerConfigRequestDto dto);
}
