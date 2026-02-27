package at.uibk.dps.coordinator.module.metric.broker.collect.abstraction;

import java.util.Map;

public interface IBrokerMetricCollector {
    Map<String, Double> collect(String brokerId);
}
