package at.uibk.dps.coordinator.module.regime.detect.business;

import at.uibk.dps.coordinator.module.regime.detect.abstraction.IChangeDetector;

public class EwmaCusumChangeDetector implements IChangeDetector {
    private final double alpha;
    private final double threshold;
    private final double drift;

    private Double ewma = null;
    private double positiveSum = 0;
    private double negativeSum = 0;

    public EwmaCusumChangeDetector(double alpha, double threshold, double drift) {
        this.alpha = alpha;
        this.threshold = threshold;
        this.drift = drift;
    }

    @Override
    public boolean detect(double value) {
        if (ewma == null) {
            ewma = value;
            return false;
        }

        ewma = alpha * value + (1 - alpha) * ewma;
        var diff = value - ewma - drift;

        positiveSum = Math.max(0, positiveSum + diff);
        negativeSum = Math.min(0, negativeSum + diff);

        if (positiveSum > threshold || Math.abs(negativeSum) > threshold) {
            positiveSum = 0;
            negativeSum = 0;
            return true;
        }

        return false;
    }
}
