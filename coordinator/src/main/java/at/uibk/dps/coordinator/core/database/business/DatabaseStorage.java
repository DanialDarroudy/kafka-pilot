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
    private final Map<String, EwmaCusumModel> ewmaCusumMetricMap = new HashMap<>();
    private final Map<String, String> currentWorkloadMap = new HashMap<>();


    @Override
    public Optional<EwmaCusumModel> getEwmaCusumModel(String instanceId, String metricName) {
        return Optional.ofNullable(ewmaCusumMetricMap.get(ewmaCusumKey(instanceId, metricName)));
    }

    @Override
    public void updateEwmaCusumModel(String instanceId, String metricName, EwmaCusumModel newModel) {
        ewmaCusumMetricMap.put(ewmaCusumKey(instanceId, metricName), newModel);
    }

    @Override
    public Optional<String> getCurrentWorkload(String instanceId) {
        return Optional.ofNullable(currentWorkloadMap.get(instanceId));
    }

    @Override
    public void updateCurrentWorkload(String instanceId, String newWorkload) {
        currentWorkloadMap.put(instanceId, newWorkload);
    }

    private String ewmaCusumKey(String instanceName, String metricName) {
        return instanceName + ":" + metricName;
    }
}
