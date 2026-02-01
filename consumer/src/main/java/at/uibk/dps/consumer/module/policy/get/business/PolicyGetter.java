package at.uibk.dps.consumer.module.policy.get.business;

import at.uibk.dps.consumer.core.config.ConsumerConfig;
import at.uibk.dps.consumer.module.policy.get.abstraction.IPolicyGetter;
import at.uibk.dps.consumer.module.policy.get.dto.GetPolicyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyGetter implements IPolicyGetter {
    private final ConsumerConfig config;

    @Override
    public GetPolicyResponseDto getPolicy() {
        return new GetPolicyResponseDto(){
            {
                setFetchMinBytes(config.getFetchMinBytes());
                setFetchMaxWaitMs(config.getFetchMaxWaitMs());
                setMaxPollRecords(config.getMaxPollRecords());
            }
        };
    }
}
