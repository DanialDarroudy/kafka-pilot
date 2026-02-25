package at.uibk.dps.coordinator.module.regime.configuration;

import lombok.Getter;

@Getter
public enum DetectionType {
    EWMA_CUSUM("ewma_cusum"),
    EVENT("event");

    private final String value;

    DetectionType(String value){
        this.value = value;
    }
}
