package ProiectMPP.ControllerInterfaces;

import ProiectMPP.Model.Employee;
import ProiectMPP.Services.IAppObserver;
import ProiectMPP.Services.IAppServices;
import javafx.stage.Stage;

public interface IMainController extends IAppObserver {
    void setServer(IAppServices givenService);
    void setServer(IAppServices givenService, Employee givenLoggedUser, Stage givenStage);
    void onBtnInscriereClick();
    void disconnect();
    void filter();
    void resetFilters();
}
