package ProiectMPP.Services;


import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IAppServices {
     void login(Employee employee, IAppObserver client) throws AppException;

     void sendChild(Child child, List<Trial> trialList) throws AppException;

     void logout(Employee employee) throws AppException;

     List<Child> getAllChildren() throws AppException;

     List<Trial> getAllTrials() throws AppException;

     List<UUID> getChildTrials(Child child) throws AppException;
}
