package ProiectMPP.Network.ProtobufProtocols;

import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProtoUtils {

    private static AppProtobuf.Employee createEmployee(Employee employee){
        return AppProtobuf.Employee.newBuilder()
                .setId(employee.getId().toString())
                .setUsername(employee.getUsername())
                .setPassword(employee.getPassword())
                .build();
    }

    private static AppProtobuf.Child createChild(Child child){
        return AppProtobuf.Child.newBuilder()
                .setId(child.getId().toString())
                .setFirstName(child.getFirstName())
                .setLastName(child.getLastName())
                .setAge(child.getAge())
                .setNoTrials(child.getNoTrials())
                .build();
    }

    private static AppProtobuf.Trial createTrial(Trial trial){
        return AppProtobuf.Trial.newBuilder()
                .setId(trial.getId().toString())
                .setTrialName(trial.getTrialName())
                .setTrialDescription(trial.getTrialDescription())
                .setDistance(trial.getDistance())
                .build();
    }


    private static AppProtobuf.ChildTrial createChildTrial(Child child, List<Trial> trials){
        return AppProtobuf.ChildTrial.newBuilder()
                .setChild(createChild(child))
                .addAllTrials(trials.stream().map(ProtoUtils::createTrial).collect(Collectors.toList()))
                .build();
    }

    public static AppProtobuf.Request createLoginRequest(Employee employee){

        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.LOGIN)
                .setEmployee(createEmployee(employee))
                .build();
    }

    public static AppProtobuf.Request createLogoutRequest(Employee employee){
        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.LOGOUT)
                .setEmployee(createEmployee(employee))
                .build();
    }

    public static AppProtobuf.Request createGetAllChildrenRequest(){
        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.GET_ALL_CHILDREN)
                .build();
    }

    public static AppProtobuf.Request createGetAllTrialsRequest(){
        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.GET_ALL_TRIALS)
                .build();
    }

    public static AppProtobuf.Request createGetChildSTrialsRequest(Child child){
        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.GET_CHILD_TRIALS)
                .setChild(createChild(child))
                .build();
    }

    public static AppProtobuf.Request createSendChildRequest(Child child, List<Trial> trialList){
        return AppProtobuf.Request.newBuilder()
                .setType(AppProtobuf.Request.Type.SEND_CHILD)
                .setChildTrial(createChildTrial(child, trialList))
                .build();
    }

    public static Child getChildFromResponse(AppProtobuf.Respone response){
        return getModelChild(response.getChildTrial().getChild());
    }

    private static Child getModelChild(AppProtobuf.Child child){
        Child modelChild = new Child(child.getFirstName(),
                child.getLastName(),
                child.getAge(),
                child.getNoTrials());
        modelChild.setId(UUID.fromString(child.getId()));
        return modelChild;
    }

    private static Trial getModelTrial(AppProtobuf.Trial trial){
        Trial modelTrial = new Trial(trial.getDistance(), trial.getTrialName(), trial.getTrialDescription());
        modelTrial.setId(UUID.fromString(trial.getId()));
        return modelTrial;
    }

    public static List<Trial> getTrialListFromResponse(AppProtobuf.Respone response){
        return response.getChildTrial().getTrialsList().stream().map(ProtoUtils::getModelTrial).collect(Collectors.toList());
    }


    public static List<UUID> getChildsTrialsFromResponse(AppProtobuf.Respone response) {
        return response.getChildTrialsID().getTrialsIDList().stream().map(UUID::fromString).collect(Collectors.toList());
    }

    public static List<Child> getChildrenFromResponse(AppProtobuf.Respone respone){
        return respone.getChildren().getChildrenList().stream().map(ProtoUtils::getModelChild).collect(Collectors.toList());
    }

    public static List<Trial> getTrialsFromResponse(AppProtobuf.Respone respone){
        return respone.getTrial().getTrialsList().stream().map(ProtoUtils::getModelTrial).collect(Collectors.toList());
    }

}
