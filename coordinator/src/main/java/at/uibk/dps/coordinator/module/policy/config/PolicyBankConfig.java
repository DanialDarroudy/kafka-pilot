package at.uibk.dps.coordinator.module.policy.config;

import lombok.Data;
import org.apache.kafka.common.record.CompressionType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "policy-bank")
public class PolicyBankConfig {
    private Map<String, WorkloadPolicy> workloads = new HashMap<>();

    @Data
    public static class WorkloadPolicy {
        private ProducerPolicy producer;
        private ConsumerPolicy consumer;
        private BrokerPolicy broker;
    }

    @Data
    public static class ProducerPolicy {
        private int batchSize;
        private int lingerMs;
        private long bufferMemory;
        private CompressionType compressionType;
    }

    @Data
    public static class ConsumerPolicy {
        private long fetchMinBytes;
        private int fetchMaxWaitMs;
        private long maxPollRecords;
    }

    @Data
    public static class BrokerPolicy {
        private int numPartitions;
        private short replicationFactor;
        private int logSegmentBytes;
        private int minInSyncReplicas;
    }
}