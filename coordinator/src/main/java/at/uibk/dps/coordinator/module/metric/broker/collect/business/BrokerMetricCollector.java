package at.uibk.dps.coordinator.module.metric.broker.collect.business;

import at.uibk.dps.coordinator.module.metric.broker.collect.abstraction.IBrokerMetricCollector;
import at.uibk.dps.coordinator.module.prometheus.query.abstraction.IPrometheusClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BrokerMetricCollector implements IBrokerMetricCollector {
    private final IPrometheusClient prometheusClient;
    private static final Map<String, String> METRIC_QUERY_MAP = Map.of(
            "disk-io",
            "avg_over_time(kafka_server_LogFlushRateAndTimeMs{broker_id=\"%s\"}[30s])",
            "network-io",
            "avg_over_time(kafka_network_RequestMetrics_RequestsPerSec{broker_id=\"%s\"}[30s])",
            "replica-lag",
            "avg_over_time(kafka_server_ReplicaManager_UnderReplicatedPartitions{broker_id=\"%s\"}[30s])",
            "partition-load",
            "avg_over_time(kafka_server_BrokerTopicMetrics_MessagesInPerSec{broker_id=\"%s\"}[30s])"
    );

    @Override
    public Map<String, Double> collect(String brokerId) {
        var metrics = new HashMap<String, Double>();
        for (var entry : METRIC_QUERY_MAP.entrySet()) {
            var metricKey = entry.getKey();
            var queryTemplate = entry.getValue();
            var query = String.format(queryTemplate, brokerId);
            var value = prometheusClient.query(query);
            metrics.put(metricKey, value);
        }

        return metrics;
    }
}