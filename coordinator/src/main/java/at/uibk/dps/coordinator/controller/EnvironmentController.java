package at.uibk.dps.coordinator.controller;

import at.uibk.dps.coordinator.core.database.abstraction.IDatabaseStorage;
import at.uibk.dps.coordinator.module.configuration.broker.apply.abstraction.IBrokerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.broker.apply.dto.ApplyBrokerConfigRequestDto;
import at.uibk.dps.coordinator.module.configuration.broker.config.BrokerConfig;
import at.uibk.dps.coordinator.module.metric.broker.collect.abstraction.IBrokerMetricCollector;
import at.uibk.dps.coordinator.module.configuration.consumer.apply.abstraction.IConsumerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.consumer.apply.dto.ApplyConsumerConfigRequestDto;
import at.uibk.dps.coordinator.module.configuration.consumer.config.ConsumerConfig;
import at.uibk.dps.coordinator.module.metric.consumer.collect.abstraction.IConsumerMetricCollector;
import at.uibk.dps.coordinator.module.configuration.producer.apply.abstraction.IProducerConfigApplier;
import at.uibk.dps.coordinator.module.configuration.producer.apply.dto.ApplyProducerConfigRequestDto;
import at.uibk.dps.coordinator.module.configuration.producer.config.ProducerConfig;
import at.uibk.dps.coordinator.module.metric.producer.collect.abstraction.IProducerMetricCollector;
import at.uibk.dps.coordinator.module.policy.config.PolicyBankConfig;
import at.uibk.dps.coordinator.module.regime.detect.abstraction.IChangeDetector;
import at.uibk.dps.coordinator.module.regime.classify.abstraction.IWorkloadClassifier;
import at.uibk.dps.coordinator.module.regime.vote.abstraction.IVoteEngine;
import io.opentelemetry.api.logs.Logger;
import io.opentelemetry.api.logs.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EnvironmentController {
    private final Logger logger;
    private final PolicyBankConfig policyBankConfig;

    private final IProducerMetricCollector producerMetricCollector;
    private final IConsumerMetricCollector consumerMetricCollector;
    private final IBrokerMetricCollector brokerMetricCollector;

    private final IProducerConfigApplier producerConfigApplier;
    private final IConsumerConfigApplier consumerConfigApplier;
    private final IBrokerConfigApplier brokerConfigApplier;

    private final IChangeDetector changeDetector;
    private final IWorkloadClassifier classifier;
    private final IVoteEngine voteEngine;
    private final IDatabaseStorage databaseStorage;

    private final ProducerConfig producerConfig;
    private final ConsumerConfig consumerConfig;
    private final BrokerConfig brokerConfig;

    @Scheduled(fixedDelay = 60000)
    public void execute() {
        logger.logRecordBuilder()
                .setAttribute("Message", "Control loop started...")
                .setSeverity(Severity.INFO)
                .emit();

        processProducers();
        processConsumers();
        processBrokers();

        logger.logRecordBuilder()
                .setAttribute("Message", "Control loop finished")
                .setSeverity(Severity.INFO)
                .emit();
    }

    private void processProducers() {
        for (var instance : producerConfig.getInstances()) {
            var producerId = instance.getPromLabel();
            var metrics = producerMetricCollector.collect(producerId);
            processInstance(producerId, metrics, InstanceType.PRODUCER);
        }
    }

    private void processConsumers() {
        for (var instance : consumerConfig.getInstances()) {
            var consumerId = instance.getPromLabel();
            var metrics = consumerMetricCollector.collect(consumerId);
            processInstance(consumerId, metrics, InstanceType.CONSUMER);
        }
    }

    private void processBrokers() {
        for (var brokerId : brokerConfig.getIds()) {
            var metrics = brokerMetricCollector.collect(brokerId);
            processInstance(brokerId, metrics, InstanceType.BROKER);
        }
    }

    private void processInstance(String instanceId, Map<String, Double> metrics, InstanceType type) {
        for (var entry : metrics.entrySet()) {
            var metricName = entry.getKey();
            var value = entry.getValue();
            var changed = changeDetector.detect(instanceId, metricName, value);
            if (changed) {
                classifier.classify(instanceId, metricName, value);
            }
        }

        var voteResult = voteEngine.vote(instanceId);
        if (voteResult.isPresent()) {
            var newWorkload = voteResult.get();

            logger.logRecordBuilder()
                    .setAttribute("Message", "Instance workload switching is occurred.")
                    .setAttribute("InstanceId", instanceId)
                    .setAttribute("newWorkload", newWorkload)
                    .emit();

            applyPolicy(type, instanceId, newWorkload);
            databaseStorage.updateCurrentWorkload(instanceId, newWorkload);
        }
    }

    private void applyPolicy(InstanceType type, String instanceId, String workload) {
        switch (type) {
            case PRODUCER -> {
                var dto = buildProducerPolicy(workload);
                producerConfigApplier.apply(instanceId, dto);
            }
            case CONSUMER -> {
                var dto = buildConsumerPolicy(workload);
                consumerConfigApplier.apply(instanceId, dto);
            }
            case BROKER -> {
                var dto = buildBrokerPolicy(workload);
                brokerConfigApplier.apply(instanceId, dto);
            }
        }
    }

    private ApplyProducerConfigRequestDto buildProducerPolicy(String workload) {
        var policy = policyBankConfig.getWorkloads().get(workload).getProducer();
        return ApplyProducerConfigRequestDto.createWithPolicy(policy);
    }

    private ApplyConsumerConfigRequestDto buildConsumerPolicy(String workload) {
        var policy = policyBankConfig.getWorkloads().get(workload).getConsumer();
        return ApplyConsumerConfigRequestDto.createWithPolicy(policy);
    }

    private ApplyBrokerConfigRequestDto buildBrokerPolicy(String workload) {
        var policy = policyBankConfig.getWorkloads().get(workload).getBroker();
        return ApplyBrokerConfigRequestDto.createWithPolicy(policy);
    }

    private enum InstanceType {
        PRODUCER,
        CONSUMER,
        BROKER
    }
}