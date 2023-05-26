package ProiectMPP.Controllers;

import ProiectMPP.ControllerInterfaces.ILoggerController;
import ProiectMPP.ControllerInterfaces.IMainController;
import ProiectMPP.Model.Employee;
import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggerController implements ILoggerController {

    boolean exec = false;

    @FXML
    private TextField usernameField;


    @FXML
    private PasswordField passwordField;

    private IMainController mainController;

    @FXML
    Text userExists;

    IAppServices server;



    Stage stage;

    @FXML
    public void initialize(){
    }

    public void setServer(IAppServices givenServer, Stage givenStage){
        this.server = givenServer;
        this.stage = givenStage;
    }

    public void setMainController(IMainController controller){
        this.mainController = controller;
    }

    @FXML
    public void logIn(){
            Employee employee = new Employee(usernameField.getText(), passwordField.getText());

            try {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Views/mainController-view.fxml"));
                Parent root = loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("DotDot");
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                IMainController loggedInController  = loader.getController();
                loggedInController.setServer(server);
                server.login(employee, loggedInController);
                loggedInController.setServer(server, employee, dialogStage);
                dialogStage.show();
                this.stage.close();


            }catch (AppException e) {
                userExists.setVisible(true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            }
    }
