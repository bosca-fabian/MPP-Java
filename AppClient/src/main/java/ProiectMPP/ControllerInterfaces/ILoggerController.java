package ProiectMPP.ControllerInterfaces;

import ProiectMPP.Controllers.MainController;
import ProiectMPP.Services.IAppServices;
import javafx.stage.Stage;

public interface ILoggerController {
    void setServer(IAppServices givenServer, Stage givenStage);
    void logIn();
    void setMainController(IMainController controller);
}
