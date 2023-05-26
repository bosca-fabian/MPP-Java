package ProiectMPP;

import ProiectMPP.Model.Child;
import ProiectMPP.Model.Trial;
import ProiectMPP.Network.Utils.AbstractServer;
import ProiectMPP.Network.Utils.AppRpcConcurrentServer;
import ProiectMPP.Network.Utils.AppServerException;
import ProiectMPP.Repos.ChildRepository;
import ProiectMPP.Repos.ChildTrialRepository;
import ProiectMPP.Repos.EmployeeRepository;
import ProiectMPP.Repos.TrialRepository;
import ProiectMPP.ReposInterfaces.IChildTrialRepository;
import ProiectMPP.ReposInterfaces.IEmployeeRepo;
import ProiectMPP.ReposInterfaces.Repository;
import ProiectMPP.Server.AppServicesImpl;
import ProiectMPP.Services.IAppServices;


import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
        Properties serverProps=new Properties();
        Properties bdProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/app.properties"));
            bdProps.load(new FileReader("AppServer/src/main/resources/bd.config"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        IEmployeeRepo employeeRepository=new EmployeeRepository(bdProps);
        Repository<Child> childRepository = new ChildRepository(bdProps);
        IChildTrialRepository childTrialRepository = new ChildTrialRepository(bdProps);
        Repository<Trial> trialRepository = new TrialRepository(bdProps);
        IAppServices chatServerImpl=new AppServicesImpl(employeeRepository, childRepository, childTrialRepository, trialRepository);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new AppRpcConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (AppServerException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.stop();
            } catch (AppServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
