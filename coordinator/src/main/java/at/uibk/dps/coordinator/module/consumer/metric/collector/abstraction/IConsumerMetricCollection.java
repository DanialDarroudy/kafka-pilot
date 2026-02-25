package at.uibk.dps.coordinator.module.consumer.metric.collector.abstraction;

import java.util.Map;

public interface IConsumerMetricCollection {
    Map<String, Double> collect(String consumerId);
}
