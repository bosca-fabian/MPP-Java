package ProiectMPP.ReposInterfaces;

import ProiectMPP.Model.Employee;

import java.util.List;
import java.util.UUID;

public interface IEmployeeRepo {

    void add(Employee entity);
    void delete(UUID entity);
    void update(Employee entity);
    int size();
    Employee readEntity(UUID entityID);
    Employee readEmployee(String username);
    List<Employee> readEntities();
}
