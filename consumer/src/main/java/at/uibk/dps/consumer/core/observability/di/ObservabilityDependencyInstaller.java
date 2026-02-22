package at.uibk.dps.consumer.core.observability.di;

import at.uibk.dps.consumer.core.observability.config.ObservabilityConfig;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ObservabilityDependencyInstaller {
    private final ObservabilityConfig config;

    @Bean
    public Meter getMeter() {
        var exporter = OtlpGrpcMetricExporter.builder()
                .setConnectTimeout(Duration.ofSeconds(config.getOtel().getConnectionTimeoutSeconds()))
                .setTimeout(Duration.ofSeconds(config.getMetric().getExportTimeoutSeconds()))
                .setEndpoint(config.getOtel().getCollectorGrpcEndpoint())
                .build();

        var reader = PeriodicMetricReader.builder(exporter)
                .setInterval(Duration.ofSeconds(config.getMetric().getReadIntervalSeconds()))
                .build();

        var provider = SdkMeterProvider.builder()
                .registerMetricReader(reader)
                .build();

        return provider.get("ConsumerMeter");
    }

    @Bean
    public Logger getLogger() {
        var exporter = OtlpGrpcLogRecordExporter.builder()
                .setConnectTimeout(Duration.ofSeconds(config.getOtel().getConnectionTimeoutSeconds()))
                .setTimeout(Duration.ofSeconds(config.getLog().getExportTimeoutSeconds()))
                .setEndpoint(config.getOtel().getCollectorGrpcEndpoint())
                .build();

        var processor = BatchLogRecordProcessor.builder(exporter)
                .setMaxExportBatchSize(config.getLog().getMaxBatchSize())
                .setMaxQueueSize(config.getLog().getMaxQueueSize())
                .build();

        var provider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(processor)
                .build();

        return provider.get("ConsumerLogger");
    }

}
