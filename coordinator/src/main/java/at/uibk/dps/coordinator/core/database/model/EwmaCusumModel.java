package at.uibk.dps.coordinator.core.database.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EwmaCusumModel {
    private Double ewma;
    private double positiveSum;
    private double negativeSum;
}
