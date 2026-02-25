package at.uibk.dps.coordinator.module.prometheus.query.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PrometheusResponseDto {
    private String status;
    private DataNode data;

    @Data
    public static class DataNode {
        private String resultType;
        private List<ResultNode> result;
    }

    @Data
    public static class ResultNode {
        private Map<String, String> metric;
        private List<Object> value;

        public double getMetricValue() {
            return Double.parseDouble(value.get(1).toString());
        }

        public double getTimestamp() {
            return Double.parseDouble(value.get(0).toString());
        }
    }
}
