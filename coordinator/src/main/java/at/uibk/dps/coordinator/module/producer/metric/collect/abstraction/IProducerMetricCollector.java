package at.uibk.dps.coordinator.module.producer.metric.collect.abstraction;

import java.util.Map;

public interface IProducerMetricCollector {
    Map<String, Double> collect(String producerId);
}
