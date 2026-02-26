package at.uibk.dps.coordinator.module.consumer.metric.collect.abstraction;

import java.util.Map;

public interface IConsumerMetricCollector {
    Map<String, Double> collect(String consumerId);
}
