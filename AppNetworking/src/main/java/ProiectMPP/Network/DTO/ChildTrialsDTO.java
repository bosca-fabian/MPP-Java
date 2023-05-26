package ProiectMPP.Network.DTO;

import ProiectMPP.Model.Trial;

import java.io.Serializable;
import java.util.List;

public class ChildTrialsDTO implements Serializable {

    public ChildDTO childDTO;

    public List<TrialDTO> trialsDTOLIst;

    public ChildTrialsDTO(ChildDTO childDTO, List<TrialDTO> trialsDTOLIst) {
        this.childDTO = childDTO;
        this.trialsDTOLIst = trialsDTOLIst;
    }
}
