package ProiectMPP.Repos;


import ProiectMPP.Model.Trial;
import ProiectMPP.ReposInterfaces.Repository;

import ProiectMPP.PersistenceUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Component
public class TrialRepository implements Repository<Trial> {

    private JdbcUtils jdbcUtils;

    private static final Logger logger= LogManager.getLogger();

    public TrialRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        this.jdbcUtils = new JdbcUtils(props);
    }

    public TrialRepository(){
        Properties bdProps = new Properties();
        try {
            bdProps.load(new FileReader("AppServer/src/main/resources/bd.config"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.jdbcUtils = new JdbcUtils(bdProps);
    }

    @Override
    public void add(Trial entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preStm = con.prepareStatement("insert into \"Trial\"(distance, \"trialName\", \"trialDescription\", id) values (?, ?, ?, ?)")){
            preStm.setInt(1,entity.getDistance());
            preStm.setString(2, entity.getTrialName());
            preStm.setString(3, entity.getTrialDescription());
            preStm.setString(4,entity.getId().toString());
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
        try(PreparedStatement preStm = con.prepareStatement("DELETE FROM \"Trial\" WHERE id = '" + entity.toString() + "';")){
            int result = preStm.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
    }

    @Override
    public void update(Trial entity) {

        String sql = "UPDATE \"Trial\" SET \"distance\" = '" + entity.getDistance() + "', " +
                "\"trialName\" = '" + entity.getTrialName() + "', " + "\"trialDescription\" =  '"
                + entity.getTrialDescription()
                + "' WHERE id = '" + entity.getId().toString()
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
        String sql = "SELECT COUNT(*) as totalTrials FROM \"Trial\"";
        try (PreparedStatement preStm = con.prepareStatement(sql)) {
            ResultSet resultSet = preStm.executeQuery();
            resultSet.next();
            return resultSet.getInt("totalTrials");
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB:" + e);
        }
        return 0;
    }


    @Override
    public Trial readEntity(UUID entityID) {
        logger.traceEntry("Reading trial {}", entityID.toString());
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStm = con.prepareStatement("select *  from \"Trial\" where id=?")) {
            preStm.setString(1, entityID.toString());
            try (ResultSet result = preStm.executeQuery()) {
                result.next();
                String id = result.getString("id");
                int distance = result.getInt("distance");
                String trialName = result.getString("trialName");
                String trialDescription = result.getString("trialDescription");
                Trial readTrial = new Trial(distance, trialName, trialDescription);
                readTrial.setId(UUID.fromString(id));
                logger.traceExit(readTrial);
                return readTrial;
            }
        } catch (SQLException ex) {
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit("No trial found with specified ID");
        return null;
    }


    @Override
    public List<Trial> readEntities() {
        logger.traceEntry("Reading trials", "Trials");
        Connection con = jdbcUtils.getConnection();
        List<Trial> trials = new ArrayList<>();
        try(PreparedStatement preStm = con.prepareStatement("select *  from \"Trial\"")){
            try(ResultSet result = preStm.executeQuery()){
                while(result.next()){
                    String id = result.getString("id");
                    int distance = result.getInt("distance");
                    String trialName = result.getString("trialName");
                    String trialDescription = result.getString("trialDescription");
                    Trial readTrial = new Trial(distance, trialName, trialDescription);
                    readTrial.setId(UUID.fromString(id));
                    trials.add(readTrial);
                }
            }
        }catch (SQLException ex){
            logger.trace(ex);
            System.err.println("DB Error: " + ex);
        }
        logger.traceExit(trials);
        return trials;
    }
}

