package ProiectMPP.ReposInterfaces;

import java.util.List;
import java.util.UUID;

public interface IChildTrialRepository {
    void addChildTrial(UUID childID, UUID trialID);
    void deleteChildTrial(UUID childID, UUID trialID);
    void deleteAllChildTrials(UUID childID);
    List<UUID> readChildTrials(UUID childID);
}
