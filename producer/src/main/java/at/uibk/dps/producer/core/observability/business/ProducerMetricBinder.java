package at.uibk.dps.producer.core.observability.business;

import at.uibk.dps.producer.module.producer.manage.abstraction.IProducerManager;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProducerMetricBinder {
    private final Meter meter;
    private final IProducerManager producerManager;
    private final List<ObservableDoubleGauge> registeredGauges = new ArrayList<>();
    private static final Map<String, String> KAFKA_METRICS = Map.of(
            "record-send-rate", "/s",
            "record-size-avg", "By",
            "outgoing-byte-rate", "By/s",
            "request-latency-avg", "ms",
            "request-latency-max", "ms",
            "record-error-rate", "/s",
            "record-retry-rate", "/s",
            "buffer-available-bytes", "By"
    );

    @PostConstruct
    public void registerKafkaMetrics() {
        KAFKA_METRICS.forEach((metricName, unit) -> {
            var otelMetricName = "kafka_producer_" + metricName.replace("-", "_");
            var gauge = meter
                    .gaugeBuilder(otelMetricName)
                    .setDescription("Kafka producer metric: " + metricName)
                    .setUnit(unit)
                    .buildWithCallback(measurement -> {
                        var producer = producerManager.getProducer();
                        if (producer == null) {
                            measurement.record(0.0);
                            return;
                        }
                        measurement.record(extractMetricValue(producer, metricName));
                    });
            registeredGauges.add(gauge);
        });
    }

    private double extractMetricValue(KafkaProducer<String, byte[]> producer, String targetMetricName) {
        try {
            for (var entry : producer.metrics().entrySet()) {
                if (entry.getKey().name().equals(targetMetricName)) {
                    var metricValue = entry.getValue().metricValue();
                    if (metricValue instanceof Number number) {
                        return number.doubleValue();
                    }
                    return 0.0;
                }
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }
}
