package at.uibk.dps.coordinator.core.database.business;

import at.uibk.dps.coordinator.core.database.abstraction.IDatabaseStorage;
import at.uibk.dps.coordinator.core.database.model.EwmaCusumModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseStorage implements IDatabaseStorage {
    private final Map<String, EwmaCusumModel> ewmaCusumMetricMap = new HashMap<>(); // key = instance:metric
    private final Map<String, String> currentWorkloadMap = new HashMap<>(); // key = instance
    private final Map<String, String> suggestedWorkloadMap = new HashMap<>(); // key = instance:metric


    @Override
    public Optional<EwmaCusumModel> getEwmaCusumModel(String instanceId, String metricName) {
        return Optional.ofNullable(ewmaCusumMetricMap.get(mergeInstanceIdWithMetricName(instanceId, metricName)));
    }

    @Override
    public void updateEwmaCusumModel(String instanceId, String metricName, EwmaCusumModel newModel) {
        ewmaCusumMetricMap.put(mergeInstanceIdWithMetricName(instanceId, metricName), newModel);
    }

    @Override
    public Optional<String> getCurrentWorkload(String instanceId) {
        return Optional.ofNullable(currentWorkloadMap.get(instanceId));
    }

    @Override
    public void updateCurrentWorkload(String instanceId, String newWorkload) {
        currentWorkloadMap.put(instanceId, newWorkload);
    }

    @Override
    public void updateSuggestedWorkload(String instanceId, String metricName, String suggestedWorkload) {
        suggestedWorkloadMap.put(mergeInstanceIdWithMetricName(instanceId, metricName), suggestedWorkload);
    }

    @Override
    public Map<String, String> getAllSuggestedWorkloadsForInstance(String instanceId) {
        var result = new HashMap<String, String>();
        suggestedWorkloadMap.forEach((key, value) -> {
            if (key.startsWith(instanceId + ":")) {
                var metricName = extractMetricName(key, instanceId);
                result.put(metricName, value);
            }
        });

        return result;
    }

    private String mergeInstanceIdWithMetricName(String instanceName, String metricName) {
        return instanceName + ":" + metricName;
    }

    private String extractMetricName(String key, String instanceId){
        return key.replace(instanceId + ":", "");
    }
}
