package at.uibk.dps.coordinator.module.regime.detect.abstraction;

public interface IChangeDetector {
    boolean detect(String instanceId, String metricName, double value);
}
