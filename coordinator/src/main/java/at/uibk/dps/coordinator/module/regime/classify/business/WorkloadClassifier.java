package at.uibk.dps.coordinator.module.regime.classify.business;

import at.uibk.dps.coordinator.core.database.abstraction.IDatabaseStorage;
import at.uibk.dps.coordinator.module.regime.classify.abstraction.IWorkloadClassifier;
import at.uibk.dps.coordinator.module.regime.classify.config.WorkloadClassificationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkloadClassifier implements IWorkloadClassifier {
    private final WorkloadClassificationConfig config;
    private final IDatabaseStorage databaseStorage;

    @Override
    public void classify(String instanceId, String metricName, double metricValue) {
        var workloadRules = config.getMetrics().get(metricName);
        for (var entry : workloadRules.entrySet()) {
            var workload = entry.getKey();
            var condition = entry.getValue();
            if (evaluate(metricValue, condition)) {
                databaseStorage.updateSuggestedWorkload(instanceId, metricName, workload);
                return;
            }
        }
    }

    private boolean evaluate(double value, WorkloadClassificationConfig.Condition condition) {
        var threshold = condition.getThreshold();
        return switch (condition.getOperator()) {
            case ">" -> value > threshold;
            case "<" -> value < threshold;
            case ">=" -> value >= threshold;
            case "<=" -> value <= threshold;
            case "==" -> value == threshold;
            default -> false;
        };
    }
}