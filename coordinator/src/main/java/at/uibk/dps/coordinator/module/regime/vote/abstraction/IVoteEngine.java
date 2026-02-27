package at.uibk.dps.coordinator.module.regime.vote.abstraction;

import java.util.Optional;

public interface IVoteEngine {
    Optional<String> vote(String instanceId);
}
