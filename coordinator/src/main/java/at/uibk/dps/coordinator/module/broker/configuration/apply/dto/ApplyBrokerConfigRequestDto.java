package at.uibk.dps.coordinator.module.broker.configuration.apply.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ApplyBrokerConfigRequestDto {
    private int minInSyncReplicas;
    private int logSegmentBytes;
    private long logRetentionMs;
    private int numNetworkThreads;
    private int numIoThreads;
    private int replicaFetchMaxBytes;

    public Map<String, String> toKafkaConfigMap() {
        var map = new HashMap<String, String>();
        map.put("min.insync.replicas", String.valueOf(minInSyncReplicas));
        map.put("log.segment.bytes", String.valueOf(logSegmentBytes));
        map.put("log.retention.ms", String.valueOf(logRetentionMs));
        map.put("num.network.threads", String.valueOf(numNetworkThreads));
        map.put("num.io.threads", String.valueOf(numIoThreads));
        map.put("replica.fetch.max.bytes", String.valueOf(replicaFetchMaxBytes));
        return map;
    }
}
