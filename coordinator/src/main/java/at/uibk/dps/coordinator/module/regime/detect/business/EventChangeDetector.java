package at.uibk.dps.coordinator.module.regime.detect.business;

import at.uibk.dps.coordinator.module.regime.detect.abstraction.IChangeDetector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventChangeDetector implements IChangeDetector {

    @Override
    public boolean detect(double value) {
        return value > 0;
    }
}
