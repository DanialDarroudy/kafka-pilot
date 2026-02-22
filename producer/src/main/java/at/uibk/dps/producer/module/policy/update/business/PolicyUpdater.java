package at.uibk.dps.producer.module.policy.update.business;

import at.uibk.dps.producer.core.config.ProducerConfig;
import at.uibk.dps.producer.module.policy.update.abstraction.IPolicyUpdater;
import at.uibk.dps.producer.module.policy.update.dto.UpdatePolicyRequestDto;
import at.uibk.dps.producer.module.producer.manage.abstraction.IProducerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyUpdater implements IPolicyUpdater {
    private final ProducerConfig config;
    private final IProducerManager producerManager;

    @Override
    public void updatePolicy(UpdatePolicyRequestDto dto) {
        config.setLingerMs(dto.getLingerMs());
        config.setBatchSize(dto.getBatchSize());
        config.setBufferMemory(dto.getBufferMemory());
        config.setCompressionType(dto.getCompressionType());
        config.setAcks(dto.getAcks());
        producerManager.policyChanged();
    }
}
