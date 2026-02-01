package at.uibk.dps.consumer.controller;

import at.uibk.dps.consumer.module.policy.get.abstraction.IPolicyGetter;
import at.uibk.dps.consumer.module.policy.get.dto.GetPolicyResponseDto;
import at.uibk.dps.consumer.module.policy.update.abstraction.IPolicyUpdater;
import at.uibk.dps.consumer.module.policy.update.dto.UpdatePolicyRequestDto;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {
    private final IPolicyUpdater policyUpdater;
    private final IPolicyGetter policyGetter;
    private final LongCounter counter;
    private final Logger logger;

    public PolicyController(IPolicyUpdater policyUpdater, IPolicyGetter policyGetter, Logger logger, Meter meter) {
        this.policyUpdater = policyUpdater;
        this.policyGetter = policyGetter;
        counter = meter.counterBuilder("consumer.reconfig.count")
                .setDescription("Number of consumer reconfigurations")
                .setUnit("1")
                .build();
        this.logger = logger;
    }

    @PutMapping()
    public ResponseEntity<Object> updatePolicy(@RequestBody UpdatePolicyRequestDto dto) {
        policyUpdater.updatePolicy(dto);

        logger.logRecordBuilder()
                .setAttribute("Message", "Updated policy with new configuration")
                .setSeverity(Severity.INFO)
                .setBody(dto.toString());

        counter.add(1);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<GetPolicyResponseDto> getPolicy() {
        return ResponseEntity.ok().body(policyGetter.getPolicy());
    }
}
