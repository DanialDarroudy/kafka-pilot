package at.uibk.dps.coordinator.module.regime.classify.abstraction;

public interface IWorkloadClassifier {
    void classify(String instanceId, String metricName, double metricValue);
}
