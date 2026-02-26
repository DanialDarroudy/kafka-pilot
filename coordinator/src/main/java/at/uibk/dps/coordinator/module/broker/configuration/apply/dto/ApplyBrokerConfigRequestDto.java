package at.uibk.dps.coordinator.module.broker.configuration.apply.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ApplyBrokerConfigRequestDto {
    private int numPartitions;
    private short replicationFactor;
    private int logSegmentBytes;
    private int minInSyncReplicas;

    public Map<String, String> toKafkaConfigMap() {
        var map = new HashMap<String, String>();
        map.put("num.partitions", String.valueOf(numPartitions));
        map.put("replication.factor", String.valueOf(replicationFactor));
        map.put("log.segment.bytes", String.valueOf(logSegmentBytes));
        map.put("min.insync.replicas", String.valueOf(minInSyncReplicas));
        return map;
    }
}
