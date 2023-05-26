package ProiectMPP.Services;



import ProiectMPP.Model.Child;
import ProiectMPP.Model.Trial;

import java.util.List;

public interface IAppObserver {

    void childReceived(Child child, List<Trial> trials) throws AppException;
}
