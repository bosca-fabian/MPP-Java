package ProiectMPP.Repos;

import ProiectMPP.ReposInterfaces.IEmployeeRepo;
import ProiectMPP.Model.Employee;
import ProiectMPP.PersistenceUtils.JdbcUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class EmployeeRepository implements IEmployeeRepo {

    private JdbcUtils jdbcUtils;

    private static final Logger logger= LogManager.getLogger();

    public EmployeeRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Employee entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("insert into \"Employee\"(id, username, password) values (?, ?, ?)")){
            preStm.setString(1,entity.getId().toString());
            preStm.setString(2, entity.getUsername());
            preStm.setString(3, entity.getPassword());
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public void delete(UUID entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("DELETE FROM \"Employee\" WHERE id = '" + entity.toString() + "';")){
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public void update(Employee entity) {

        String sql = "UPDATE \"Employee\" SET \"username\" = '" + entity.getUsername() + "', " +
                "\"password\" = '" + entity.getPassword() + "' WHERE id = '" + entity.getId().toString()
                + "';";

        logger.traceEntry("Saving update task {}", entity);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement(sql)) {
            int result = preStm.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("BD Error: " + e);
        }
    }

    @Override
    public int size() {
        logger.traceEntry("Saving task {}", "size");
        Connection con = jdbcUtils.getConnection();
        String sql = "SELECT COUNT(*) as totalEmployees FROM \"Employee\"";
        try (PreparedStatement preStm = con.prepareStatement(sql)) {
            ResultSet resultSet = preStm.executeQuery();
            resultSet.next();
            return resultSet.getInt("totalEmployees");
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB:" + e);
        }
        return 0;
    }

    @Override
    public Employee readEntity(UUID entityID) {
        logger.traceEntry("Reading employee {} ", entityID.toString());
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select *  from \"Employee\" where id=?")) {
            preStm.setString(1, entityID.toString());
            try (ResultSet result = preStm.executeQuery()) {
                result.next();
                String id = result.getString("id");
                String username = result.getString("username");
                String password = result.getString("password");
                Employee readEmployee = new Employee(username, password);
                readEmployee.setId(UUID.fromString(id));
                logger.traceExit(readEmployee);
                return readEmployee;
            }
        } catch (SQLException ex) {
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit("No employee found with specified ID");
        return null;
    }

    public Employee readEmployee(String employeeUsername) {
        logger.traceEntry("Reading employee {} ",employeeUsername);
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select *  from \"Employee\" where \"username\" =?")) {
            preStm.setString(1, employeeUsername);
            try (ResultSet result = preStm.executeQuery()) {
                result.next();
                String id = result.getString("id");
                String username = result.getString("username");
                String password = result.getString("password");
                Employee readEmployee = new Employee(username, password);
                readEmployee.setId(UUID.fromString(id));
                logger.traceExit(readEmployee);
                return readEmployee;
            }
        } catch (SQLException ex) {
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit("No employee found with specified ID");
        return null;
    }


    @Override
    public List<Employee> readEntities() {
        logger.traceEntry("Reading employees", "Employees");
        Connection con = jdbcUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try(PreparedStatement preStm = con.prepareStatement("select *  from \"Employee\"")){
            try(ResultSet result = preStm.executeQuery()){
                while(result.next()){
                    String id = result.getString("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Employee readEmployee = new Employee(username, password);
                    readEmployee.setId(UUID.fromString(id));
                    employees.add(readEmployee);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit(employees);
        return employees;
    }


}
