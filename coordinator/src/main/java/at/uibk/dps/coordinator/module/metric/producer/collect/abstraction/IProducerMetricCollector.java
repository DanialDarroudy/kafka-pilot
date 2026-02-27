package at.uibk.dps.coordinator.module.metric.producer.collect.abstraction;

import java.util.Map;

public interface IProducerMetricCollector {
    Map<String, Double> collect(String producerId);
}
