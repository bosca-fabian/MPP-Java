package ProiectMPP.Repos;


import ProiectMPP.PersistenceUtils.JdbcUtils;
import ProiectMPP.ReposInterfaces.IChildTrialRepository;
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

public class ChildTrialRepository implements IChildTrialRepository {

    private JdbcUtils jdbcUtils;

    private static final Logger logger = LogManager.getLogger();

    public ChildTrialRepository(Properties props){
        logger.info("Initializing ChildTrialRepository with properties: {} ", props);
        jdbcUtils = new JdbcUtils(props);
    }


    public void addChildTrial(UUID childID, UUID trialID){
        logger.traceEntry("saving task {} {} ", childID, trialID);
        Connection con = jdbcUtils.getConnection();
        String sql = "INSERT INTO \"ChildTrial\"(\"Child_ID\", \"Trial_ID\") VALUES (?, ?)";
        try(PreparedStatement preStm = con.prepareStatement(sql)){
            preStm.setString(1, childID.toString());
            preStm.setString(2, trialID.toString());
            int result = preStm.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            System.err.println("BD ERROR" + e);
            logger.error(e);
        }
    }

    public void deleteChildTrial(UUID childID, UUID trialID){
        logger.trace("Saving task {} {}", childID, trialID);
        Connection con = jdbcUtils.getConnection();
        String sql = "DELETE FROM \"ChildTrial\" WHERE \"Child_ID\" = ? AND \"Trial_ID\" = ?";
        try(PreparedStatement preStm = con.prepareStatement(sql)){
            preStm.setString(1, childID.toString());
            preStm.setString(2, trialID.toString());
            int result = preStm.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            System.err.println("BD ERROR" + e);
            logger.error(e);
        }
    }

    public void deleteAllChildTrials(UUID childID){
        logger.trace("Saving task {}", childID);
        Connection con = jdbcUtils.getConnection();
        String sql = "DELETE FROM \"ChildTrial\" WHERE \"Child_ID\" = ?";
        try(PreparedStatement preStm = con.prepareStatement(sql)){
            preStm.setString(1, childID.toString());
            int result = preStm.executeUpdate();
            logger.traceExit(result);
        } catch (SQLException e) {
            System.err.println("BD ERROR" + e);
            logger.error(e);
        }
    }

    public List<UUID> readChildTrials(UUID childID){
        logger.trace("Saving task {}", childID);
        Connection con = jdbcUtils.getConnection();
        List<UUID> childTrials = new ArrayList<>();
        String sql = "SELECT * FROM \"ChildTrial\" WHERE \"Child_ID\" = ?";
        try(PreparedStatement preStm = con.prepareStatement(sql)){
            preStm.setString(1, childID.toString());
            try(ResultSet result = preStm.executeQuery()){
                while(result.next()) {
                    String trialID = result.getString(2);
                    childTrials.add(UUID.fromString(trialID));
                }
            }

        } catch (SQLException e) {
            System.err.println("BD ERROR" + e);
            logger.error(e);
        }
        logger.traceExit(childTrials);
        return childTrials;
    }

}
