package ProiectMPP.Network.DTO;

import java.io.Serializable;
import java.util.UUID;

public class TrialDTO implements Serializable {
    public UUID id;
    public String trialName;
    public String trialDescription;
    public int distance;

    public TrialDTO(UUID id, String trialName, String trialDescription, int distance)
    {
        this.id = id;
        this.trialDescription = trialDescription;
        this.trialName = trialName;
        this.distance = distance;
    }
}
