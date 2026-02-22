package at.uibk.dps.consumer.core.observability.business;

import at.uibk.dps.consumer.module.consumer.manage.abstraction.IConsumerManager;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConsumerMetricBinder {
    private final Meter meter;
    private final IConsumerManager consumerManager;
    private final List<ObservableDoubleGauge> registeredGauges = new ArrayList<>();
    private static final Map<String, String> KAFKA_METRICS = Map.of(
            "records-lag-max", "records",
            "fetch-rate", "/s",
            "commit-rate", "/s",
            "join-total", "1"
    );

    @PostConstruct
    public void registerKafkaMetrics() {
        KAFKA_METRICS.forEach((metricName, unit) -> {
            var otelMetricName = "kafka_consumer_" + metricName.replace("-", "_");
            var gauge = meter
                    .gaugeBuilder(otelMetricName)
                    .setDescription("Kafka consumer metric: " + metricName)
                    .setUnit(unit)
                    .buildWithCallback(measurement -> {
                        var consumer = consumerManager.getConsumer();
                        if (consumer == null) {
                            measurement.record(0.0);
                            return;
                        }
                        measurement.record(extractMetricValue(consumer, metricName));
                    });
            registeredGauges.add(gauge);
        });
    }

    private double extractMetricValue(KafkaConsumer<String, byte[]> consumer, String targetMetricName) {
        try {
            for (var entry : consumer.metrics().entrySet()) {
                if (entry.getKey().name().equals(targetMetricName)) {
                    var metricValue = entry.getValue().metricValue();
                    if (metricValue instanceof Number number) {
                        return number.doubleValue();
                    }
                    return 0.0;
                }
            }
        } catch (Exception ignored) {
            return 0.0;
        }
        return 0.0;
    }
}
