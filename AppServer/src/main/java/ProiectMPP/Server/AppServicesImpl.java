package ProiectMPP.Server;

import ProiectMPP.ReposInterfaces.IChildTrialRepository;
import ProiectMPP.ReposInterfaces.IEmployeeRepo;
import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;
import ProiectMPP.Repos.ChildRepository;
import ProiectMPP.Repos.ChildTrialRepository;
import ProiectMPP.Repos.TrialRepository;
import ProiectMPP.ReposInterfaces.Repository;
import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppServices;
import ProiectMPP.Services.IAppObserver;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class AppServicesImpl implements IAppServices {

    private IEmployeeRepo employeeRepository;

    private Repository<Child> childRepository;

    private IChildTrialRepository childTrialRepository;

    private Repository<Trial> trialRepository;

    private Map<UUID, IAppObserver> loggedEmployees;


    public AppServicesImpl(IEmployeeRepo eRepo, Repository<Child> childRepository, IChildTrialRepository childTrialRepository, Repository<Trial> trialRepository) {

        this.employeeRepository = eRepo;
        this.childRepository = childRepository;
        this.childTrialRepository = childTrialRepository;
        this.trialRepository = trialRepository;
        loggedEmployees = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(Employee employee, IAppObserver client) throws AppException {
        Employee employeeR = employeeRepository.readEmployee(employee.getUsername());
        if (employeeR!=null){
            if(!Objects.equals(employeeR.getPassword(), employee.getPassword()))
                throw new AppException("Wrong password");
            else if (loggedEmployees.get(employeeR.getId())!=null) {
                throw new AppException("employee already logged in.");
            }
            loggedEmployees.put(employeeR.getId(), client);
        }else
            throw new AppException("Authentication failed.");
    }

    @Override
    public synchronized void sendChild(Child child, List<Trial> trialList) throws AppException {

        if(loggedEmployees.isEmpty()){
            throw new AppException("Nobody is logged in");
        }

        this.childRepository.add(child);
        for (Trial trial:trialList
        ) {
            this.childTrialRepository.addChildTrial(child.getId(), trial.getId());
        }
        for (IAppObserver iAppObserver : loggedEmployees.values()) {
            iAppObserver.childReceived(child, trialList);
        }

    }

    @Override
    public synchronized void logout(Employee employee) throws AppException {
        Employee employeeR = employeeRepository.readEmployee(employee.getUsername());
        IAppObserver localClient = loggedEmployees.remove(employeeR.getId());
        if (localClient==null)
            throw new AppException("User "+employee.getId()+" is not logged in.");
    }

    @Override
    public synchronized List<Child> getAllChildren() throws AppException {
        List<Child> children = this.childRepository.readEntities();
        if (children.isEmpty())
            throw new AppException("There are no children!");
        return children;
    }

    @Override
    public synchronized List<Trial> getAllTrials() throws AppException {
        List<Trial> trials = this.trialRepository.readEntities();
        if (trials.isEmpty())
            throw new AppException("There are no children!");
        return trials;
    }

    @Override
    public List<UUID> getChildTrials(Child child){
        List<UUID> childTrials = this.childTrialRepository.readChildTrials(child.getId());
        return childTrials;
    }


    private final int defaultThreadsNo=5;

}
