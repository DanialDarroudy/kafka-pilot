package at.uibk.dps.coordinator.module.prometheus.query.abstraction;

public interface IPrometheusClient {
    double query(String promQl);
}
