package ProiectMPP.Repos;

import ProiectMPP.Model.Child;
import ProiectMPP.ReposInterfaces.Repository;
import ProiectMPP.PersistenceUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class ChildRepository implements Repository<Child> {

    private JdbcUtils jdbcUtils;

    private static final Logger logger = LogManager.getLogger();

    public ChildRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        this.jdbcUtils = new JdbcUtils(props);
    }


    @Override
    public void add(Child entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("insert into \"Child\" (id, \"firstName\", \"lastName\", \"age\") values (?, ?, ?, ?)")){
            preStm.setString(1,entity.getId().toString());
            preStm.setString(2, entity.getFirstName());
            preStm.setString(3, entity.getLastName());
            preStm.setInt(4, entity.getAge());
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
        try(PreparedStatement preStm = con.prepareStatement("DELETE FROM \"Child\" WHERE id = '" + entity.toString() + "';")){
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public void update(Child entity) {

        String sql = "UPDATE \"Child\" SET \"firstName\" = '" + entity.getFirstName() + "', " +
                "\"lastName\" = '" + entity.getLastName() + "', " + "age = '" + entity.getAge() + "', " +
                " WHERE id = '" + entity.getId().toString()
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
        String sql = "SELECT COUNT(*) as totalChildren FROM \"Child\"";
        try (PreparedStatement preStm = con.prepareStatement(sql)) {
            ResultSet resultSet = preStm.executeQuery();
            resultSet.next();
            int totalChildren = resultSet.getInt("totalChildren");
            logger.traceExit(totalChildren);
            return totalChildren;
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB:" + e);
        }
        logger.traceExit("Total children = 0");
        return 0;
    }

    @Override
    public Child readEntity(UUID entityID) {
        logger.traceEntry("Reading child {}", entityID.toString());
        Connection con = jdbcUtils.getConnection();
            try (PreparedStatement preStm = con.prepareStatement("select *  from \"Child\" where id=?")) {
            preStm.setString(1, entityID.toString());
            try (ResultSet result = preStm.executeQuery()) {
                result.next();
                String id = result.getString("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                int age = result.getInt("age");
                int noTrials = result.getInt("noTrials");
                Child readChild = new Child(firstName, lastName, age, noTrials);
                readChild.setId(UUID.fromString(id));
                logger.traceExit(readChild);
                return readChild;
            }
        } catch (SQLException ex) {
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit("No child found with specified ID");
        return null;
    }


    @Override
    public List<Child> readEntities() {
        logger.traceEntry("Reading children {}", "Children");
        Connection con = jdbcUtils.getConnection();
        List<Child> children = new ArrayList<>();
        try(PreparedStatement preStm = con.prepareStatement("select *  from \"Child\"")){
            try(ResultSet result = preStm.executeQuery()){
                while(result.next()){
                    String id = result.getString("id");
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    int age = result.getInt("age");
                    int noTrials = result.getInt("noTrials");
                    Child readChild = new Child(firstName, lastName, age, noTrials);
                    readChild.setId(UUID.fromString(id));
                    children.add(readChild);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit(children);
        return children;
    }
}
