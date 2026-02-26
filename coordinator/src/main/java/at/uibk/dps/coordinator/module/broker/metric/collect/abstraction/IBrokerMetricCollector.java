package at.uibk.dps.coordinator.module.broker.metric.collect.abstraction;

import java.util.Map;

public interface IBrokerMetricCollector {
    Map<String, Double> collect(String brokerId);
}
