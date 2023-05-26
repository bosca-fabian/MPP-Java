package ProiectMPP.Network.DTO;

import java.io.Serializable;
import java.util.UUID;

public class ChildDTO implements Serializable {
    public UUID id;
    public String firstName;
    public String lastName;
    public int age;
    public int noTrials;

    public ChildDTO(UUID id, String firstName, String lastName, int age, int noTrials) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.noTrials = noTrials;
    }

  

    @Override
    public String toString() {
        return "ChildDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", noTrials=" + noTrials +
                '}';
    }
}
