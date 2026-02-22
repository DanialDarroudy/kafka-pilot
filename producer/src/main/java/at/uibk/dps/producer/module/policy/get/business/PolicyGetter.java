package at.uibk.dps.producer.module.policy.get.business;

import at.uibk.dps.producer.core.config.ProducerConfig;
import at.uibk.dps.producer.module.policy.get.abstraction.IPolicyGetter;
import at.uibk.dps.producer.module.policy.get.dto.GetPolicyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyGetter implements IPolicyGetter {
    private final ProducerConfig config;

    @Override
    public GetPolicyResponseDto getPolicy() {
        return new GetPolicyResponseDto(){
            {
                setLingerMs(config.getLingerMs());
                setBatchSize(config.getBatchSize());
                setBufferMemory(config.getBufferMemory());
                setCompressionType(config.getCompressionType());
                setAcks(config.getAcks());
            }
        };
    }
}
