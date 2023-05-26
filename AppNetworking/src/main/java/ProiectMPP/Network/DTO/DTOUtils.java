package ProiectMPP.Network.DTO;

import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DTOUtils {
    public static Employee getFromDTO(EmployeeDTO empDTO){
        UUID id = empDTO.id;
        String username = empDTO.username;
        String password = empDTO.password;
        Employee newEmployee = new Employee(username, password);
        newEmployee.setId(id);
        return newEmployee;
    }

    public static EmployeeDTO getDTO(Employee employee){
        UUID id = employee.getId();
        String username = employee.getUsername();
        String password = employee.getPassword();
        return new EmployeeDTO(id, username, password);
    }

    public static Child getFromDTO(ChildDTO childDTO){
        UUID id = childDTO.id;
        String firstName = childDTO.firstName;
        String lastName = childDTO.lastName;
        int age = childDTO.age;
        int noTrials = childDTO.noTrials;
        Child newChild = new Child(firstName, lastName, age, noTrials);
        newChild.setId(id);
        return newChild;

    }

    public static ChildDTO getDTO(Child child){
        UUID id = child.getId();
        String firstName = child.getFirstName();
        String lastName = child.getLastName();
        int age = child.getAge();
        int noTrials = child.getNoTrials();
        return new ChildDTO(id, firstName, lastName, age , noTrials);
    }

    public static TrialDTO getDTO(Trial trial){
        UUID id = trial.getId();
        String trialName = trial.getTrialName();
        String trialDescription = trial.getTrialDescription();
        int trialDistance = trial.getDistance();
        return new TrialDTO(id, trialName, trialDescription, trialDistance);
    }


    public static Trial getFromDTO(TrialDTO trialDTO){
        UUID id = trialDTO.id;
        String trialName = trialDTO.trialName;
        String trialDescription = trialDTO.trialDescription;
        int trialDistance = trialDTO.distance;
        Trial newTrial = new Trial(trialDistance, trialName, trialDescription);
        newTrial.setId(id);
        return newTrial;
    }

    public static ChildTrialsDTO getDTO(Child child, List<Trial> trials){
        ChildDTO childDTO = getDTO(child);
        List<TrialDTO>  trialsDTO= new ArrayList<>();
        for (Trial trial: trials
             ) {
            trialsDTO.add(getDTO(trial));
        }
        return new ChildTrialsDTO(childDTO, trialsDTO);
    }

    public static Child getChildFromChildTrialsDTO(ChildTrialsDTO childTrialsDTO){
        return getFromDTO(childTrialsDTO.childDTO);
    }

    public static List<Trial> getTrialsFromChildTrialsDTO(ChildTrialsDTO childTrialsDTO){
        List<Trial> trials = new ArrayList<>();
        for (TrialDTO trialDTO: childTrialsDTO.trialsDTOLIst
             ) {
            trials.add(getFromDTO(trialDTO));
        }
        return trials;
    }


}
