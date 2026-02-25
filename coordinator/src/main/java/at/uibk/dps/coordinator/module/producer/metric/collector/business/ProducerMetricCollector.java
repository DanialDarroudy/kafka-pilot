package at.uibk.dps.coordinator.module.producer.metric.collector.business;

import at.uibk.dps.coordinator.module.producer.metric.collector.abstraction.IProducerMetricCollector;
import at.uibk.dps.coordinator.module.prometheus.query.abstraction.IPrometheusClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProducerMetricCollector implements IProducerMetricCollector {
    private final IPrometheusClient prometheusClient;
    private static final List<String> METRIC_QUERIES = List.of(
            "rate(kafka_producer_record_send_rate{producer_id=\"%s\"}[30s])",
            "rate(kafka_producer_outgoing_byte_rate{producer_id=\"%s\"}[30s])",
            "rate(kafka_producer_record_error_rate{producer_id=\"%s\"}[30s])",
            "rate(kafka_producer_record_retry_rate{producer_id=\"%s\"}[30s])",
            "avg_over_time(kafka_producer_record_size_avg{producer_id=\"%s\"}[30s])",
            "avg_over_time(kafka_producer_request_latency_avg{producer_id=\"%s\"}[30s])",
            "avg_over_time(kafka_producer_request_latency_max{producer_id=\"%s\"}[30s])",
            "kafka_producer_buffer_available_bytes{producer_id=\"%s\"}"
    );

    @Override
    public Map<String, Double> collect(String producerId) {
        var metrics = new HashMap<String, Double>();
        for (var queryTemplate : METRIC_QUERIES) {
            var query = String.format(queryTemplate, producerId);
            var value = prometheusClient.query(query);
            var metricKey = extractMetricKey(queryTemplate);
            metrics.put(metricKey, value);
        }

        return metrics;
    }

    private String extractMetricKey(String queryTemplate) {
        var start = queryTemplate.indexOf("kafka_producer_") + "kafka_producer_".length();
        var end = queryTemplate.indexOf("{");
        return queryTemplate.substring(start, end).replace("_", "-");
    }
}
