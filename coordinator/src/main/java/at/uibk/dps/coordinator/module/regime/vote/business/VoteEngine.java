package at.uibk.dps.coordinator.module.regime.vote.business;

import at.uibk.dps.coordinator.core.database.abstraction.IDatabaseStorage;
import at.uibk.dps.coordinator.module.regime.vote.abstraction.IVoteEngine;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VoteEngine implements IVoteEngine {
    private final Logger logger;
    private final IDatabaseStorage databaseStorage;

    @Override
    public Optional<String> vote(String instanceId) {
        var suggestions = databaseStorage.getAllSuggestedWorkloadsForInstance(instanceId);
        if (suggestions.isEmpty()) {
            logger.logRecordBuilder()
                    .setSeverity(Severity.INFO)
                    .setAttribute("Message", "Suggested workload is empty")
                    .setAttribute("InstanceId", instanceId)
                    .emit();
            return Optional.empty();
        }

        var currentWorkload = databaseStorage.getCurrentWorkload(instanceId);
        var voteCount = new HashMap<String, Integer>();
        for (var workload : suggestions.values()) {
            voteCount.merge(workload, 1, Integer::sum);
        }

        var totalVotes = voteCount.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        logger.logRecordBuilder()
                .setSeverity(Severity.INFO)
                .setAttribute("Message", "Total votes")
                .setAttribute("InstanceId", instanceId)
                .setAttribute("TotalVotes", totalVotes)
                .emit();

        for (var entry : voteCount.entrySet()) {
            var workload = entry.getKey();
            var count = entry.getValue();
            var majority = count > totalVotes / voteCount.size();
            var differentFromCurrent = currentWorkload.isEmpty() || !workload.equals(currentWorkload.get());
            if (majority && differentFromCurrent) {
                return Optional.of(workload);
            }
        }

        return Optional.empty();
    }
}
