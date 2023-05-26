package ProiectMPP.Controllers;


import ProiectMPP.ControllerInterfaces.IAddChildToTrialController;
import ProiectMPP.ControllerInterfaces.ILoggerController;
import ProiectMPP.ControllerInterfaces.IMainController;
import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;
import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppObserver;
import ProiectMPP.Services.IAppServices;
import ProiectMPP.ModelUtils.AgeBrackets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController implements IMainController {

    Stage currentStage;

    Employee loggedEmployee;



    @FXML
    BorderPane mainPane;

    @FXML
    AnchorPane mainAchor;

    @FXML
    TableView<Child> childrenTable;

    IAppServices server;

    @FXML
    TableColumn<Child, String> firstName;

    @FXML
            TableColumn<Child, String> lastName;

    @FXML
            TableColumn<Child, Integer> age;

    @FXML
            TableColumn<Child, Integer> noTrials;

    @FXML
            ChoiceBox<Trial> trial;
    @FXML
            ChoiceBox<AgeBrackets> ageBrackets;
    ObservableList<Child> children = FXCollections.observableArrayList();

    List<Child> childList = new ArrayList<>();


    @FXML
    public void initialize(){
        childrenTable.setItems(children);
    }

    private void initTable() throws AppException {
//        this.noTrials.
        this.childList.addAll(this.server.getAllChildren());
        this.children.setAll(childList);
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        noTrials.setCellValueFactory(new PropertyValueFactory<>("noTrials"));
        this.childrenTable.setItems(children);
        childrenTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @Override
    public void childReceived(Child child, List<Trial> trials) throws AppException {
        this.childList.add(child);
        this.children.add(child);
        this.childrenTable.setItems(children);
    }

    private void initChoiceBoxes() throws AppException {
        this.ageBrackets.getItems().add(AgeBrackets.NO_AGE_RESTRICTION);
        this.ageBrackets.getItems().add(AgeBrackets.SIX_EIGHT);
        this.ageBrackets.getItems().add(AgeBrackets.NINE_ELEVEN);
        this.ageBrackets.getItems().add(AgeBrackets.TWELVE_FIFTEEN);
        List<Trial> trialList = this.server.getAllTrials();
        this.trial.getItems().addAll(trialList);
    }

    public void setServer(IAppServices givenService){
        this.server = givenService;
    }


    public void setServer(IAppServices givenService, Employee givenLoggedUser, Stage givenStage) {
        this.loggedEmployee = givenLoggedUser;
        this.server = givenService;
        this.currentStage = givenStage;
        currentStage.setHeight(438);
        currentStage.setWidth(610);
        mainAchor.requestFocus();
        try {
            initTable();
            initChoiceBoxes();
        }catch (AppException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void onBtnInscriereClick() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/addChildToTrial-view.fxml"));
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("DotDot");
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            IAddChildToTrialController childToTrialController = loader.getController();
            childToTrialController.setService(this.server);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    public void disconnect() {
        try {
            FXMLLoader loader = new FXMLLoader();
            this.server.logout(loggedEmployee);
            loader.setLocation(getClass().getResource("/Views/loggercontroller-view.fxml"));
            AnchorPane root = loader.load();
            Stage loggerStage = new Stage();
            loggerStage.setTitle("DotDot");
            Scene scene = new Scene(root);
            loggerStage.setScene(scene);
            ILoggerController loggerController = loader.getController();
            loggerController.setServer(server, loggerStage);
            loggerStage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }

    public void filter() {
        AgeBrackets selectedAge = this.ageBrackets.getValue();
        if (selectedAge == null || selectedAge.equals(AgeBrackets.NO_AGE_RESTRICTION))
            children.setAll(childList);
        else {
            children.setAll(childList);
            children.removeIf(child -> child.getAge() > selectedAge.getUpperBound() || child.getAge() < selectedAge.getLowerBound());
        }
        Trial selectedTrial = this.trial.getValue();
            children.removeIf(child -> {
                try {
                    return !this.server.getChildTrials(child).contains(selectedTrial.getId());
                } catch (AppException e) {
                    return false;
                }
            });

    }

    public void resetFilters(){
        this.children.setAll(this.childList);
        this.childrenTable.setItems(children);
        this.ageBrackets.getSelectionModel().clearSelection();
        ageBrackets.setValue(null);
        this.trial.getSelectionModel().clearSelection();
        trial.setValue(null);
    }


}

