package at.uibk.dps.coordinator.core.database.abstraction;

import at.uibk.dps.coordinator.core.database.model.EwmaCusumModel;

import java.util.Optional;

public interface IDatabaseStorage {
    Optional<EwmaCusumModel> getEwmaCusumModel(String instanceId, String metricName);
    void updateEwmaCusumModel(String instanceId, String metricName, EwmaCusumModel newModel);
    Optional<String> getCurrentWorkload(String instanceId);
    void updateCurrentWorkload(String instanceId, String newWorkload);
}
