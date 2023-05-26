package ProiectMPP.Controllers;


import ProiectMPP.ControllerInterfaces.IAddChildToTrialController;
import ProiectMPP.Model.Child;
import ProiectMPP.Model.Trial;

import ProiectMPP.Model.Validators.ChildValidator;
import ProiectMPP.Model.Validators.IChildValidator;
import ProiectMPP.Model.Validators.ValidationException;
import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddChildToTrialController implements IAddChildToTrialController {
    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    TextField age;

    @FXML
    TableColumn<Trial, Integer> distance;

    @FXML
    TableColumn<Trial, String> description;

    @FXML
    TableColumn<Trial, String> name;

    @FXML
    TableView<Trial> trials;

    @FXML
    Button addToCompetition;

    @FXML
    Label validationException;


    IAppServices service;

    ObservableList<Trial> trialList = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        trials.setItems(trialList);
        trials.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void trialHandler() throws AppException {
        name.setCellValueFactory(new PropertyValueFactory<>("trialName"));
        description.setCellValueFactory(new PropertyValueFactory<>("trialDescription"));
        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        trialList.setAll(service.getAllTrials());
    }

    public void setService(IAppServices service){
        this.service = service;
        try{
            trialHandler();
        } catch (AppException e){
            System.err.println(e.getMessage());
        }

    }

    @FXML
    public void setAddToCompetition(){
        String childFirstName = this.firstName.getText();
        String childLastName = this.lastName.getText();
        String childAge = this.age.getText();


        try {

            IChildValidator validator = new ChildValidator();
            validator.validate(childFirstName, childLastName, childAge);

            ObservableList<Trial> selectedTrials = trials.getSelectionModel().getSelectedItems();
            Child child = new Child(childFirstName, childLastName, Integer.parseInt(childAge));
            this.service.sendChild(child, selectedTrials);

            this.validationException.setText("");
        }catch (ValidationException exception){
            validationException.setText(exception.getMessage());
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }
}
