package ProiectMPP.Model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Objects;


public class Trial extends Entity implements Serializable {

    private int distance;
    private String trialName;
    private String trialDescription;

    public Trial(){

    }

    public Trial(int distance, String trialName, String trialDescription) {
        this.distance = distance;
        this.trialName = trialName;
        this.trialDescription = trialDescription;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public String getTrialDescription() {
        return trialDescription;
    }

    public void setTrialDescription(String trialDescription) {
        this.trialDescription = trialDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trial trial = (Trial) o;
        return distance == trial.distance && Objects.equals(trialName, trial.trialName) && Objects.equals(trialDescription, trial.trialDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, trialName, trialDescription);
    }

    @Override
    public String toString() {
        return "Trial: " + trialName + ' ' +  distance + '(' + trialDescription + ')';
    }
}
