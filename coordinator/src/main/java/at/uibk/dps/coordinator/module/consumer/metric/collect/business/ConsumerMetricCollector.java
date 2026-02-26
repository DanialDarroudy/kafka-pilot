package at.uibk.dps.coordinator.module.consumer.metric.collect.business;

import at.uibk.dps.coordinator.module.consumer.metric.collect.abstraction.IConsumerMetricCollector;
import at.uibk.dps.coordinator.module.prometheus.query.abstraction.IPrometheusClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConsumerMetricCollector implements IConsumerMetricCollector {
    private final IPrometheusClient prometheusClient;
    private static final List<String> METRIC_QUERIES = List.of(
            "avg_over_time(kafka_consumer_records_lag_max{consumer_id=\"%s\"}[30s])",
            "rate(kafka_consumer_fetch_rate{consumer_id=\"%s\"}[30s])",
            "rate(kafka_consumer_commit_rate{consumer_id=\"%s\"}[30s])",
            "increase(kafka_consumer_join_total{consumer_id=\"%s\"}[30s])"
    );

    @Override
    public Map<String, Double> collect(String consumerId) {
        var metrics = new HashMap<String, Double>();
        for (var queryTemplate : METRIC_QUERIES) {
            var query = String.format(queryTemplate, consumerId);
            var value = prometheusClient.query(query);
            var metricKey = extractMetricKey(queryTemplate);
            metrics.put(metricKey, value);
        }

        return metrics;
    }

    private String extractMetricKey(String queryTemplate) {
        var start = queryTemplate.indexOf("kafka_consumer_") + "kafka_consumer_".length();
        var end = queryTemplate.indexOf("{");
        return queryTemplate.substring(start, end).replace("_", "-");
    }
}
