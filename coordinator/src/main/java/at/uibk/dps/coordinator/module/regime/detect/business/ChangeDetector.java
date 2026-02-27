package at.uibk.dps.coordinator.module.regime.detect.business;

import at.uibk.dps.coordinator.core.database.abstraction.IDatabaseStorage;
import at.uibk.dps.coordinator.core.database.model.EwmaCusumModel;
import at.uibk.dps.coordinator.module.regime.detect.config.DetectionType;
import at.uibk.dps.coordinator.module.regime.detect.config.RegimeDetectionConfig;
import at.uibk.dps.coordinator.module.regime.detect.abstraction.IChangeDetector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeDetector implements IChangeDetector {
    private final IDatabaseStorage databaseStorage;
    private final RegimeDetectionConfig config;

    @Override
    public boolean detect(String instanceId, String metricName, double value) {
        var detectionConfig = config.getMetricDetectionMap().get(metricName);
        if (detectionConfig.getType() == DetectionType.EVENT){
            return value > detectionConfig.getEventThreshold();
        }

        var existingModel = databaseStorage.getEwmaCusumModel(instanceId, metricName);
        var model = existingModel.orElse(
                EwmaCusumModel.builder()
                        .ewma(value)
                        .positiveSum(0)
                        .negativeSum(0)
                        .build()
        );

        var alpha = detectionConfig.getEwmaAlpha();
        var previousEwma = model.getEwma();
        var newEwma = alpha * value + (1 - alpha) * previousEwma;

        var drift = detectionConfig.getCusumDrift();
        var threshold = detectionConfig.getCusumThreshold();

        var diff = value - newEwma - drift;

        var positiveSum = Math.max(0, model.getPositiveSum() + diff);
        var negativeSum = Math.min(0, model.getNegativeSum() + diff);

        var changeDetected = positiveSum > threshold || Math.abs(negativeSum) > threshold;
        if (changeDetected) {
            positiveSum = 0;
            negativeSum = 0;
        }

        var updatedModel = EwmaCusumModel.builder()
                .ewma(newEwma)
                .positiveSum(positiveSum)
                .negativeSum(negativeSum)
                .build();
        databaseStorage.updateEwmaCusumModel(instanceId, metricName, updatedModel);
        return changeDetected;
    }
}
