package ProiectMPP;

import ProiectMPP.ControllerInterfaces.ILoggerController;
import ProiectMPP.ControllerInterfaces.IMainController;
import ProiectMPP.Controllers.LoggerController;
import ProiectMPP.Controllers.MainController;
import ProiectMPP.Network.ProtobufProtocols.AppServicesProtoProxy;
import ProiectMPP.Services.IAppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ClientStart extends Application {
    private Stage primaryStage;

    Parent root;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(new FileReader("D:\\Altele de pe la faculta\\Sem4\\MPP\\Laburi\\ProiectMPP\\AppClient2\\src\\main\\resources\\client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

//        IAppServices server = new AppServicesRpcProxy(serverIP, serverPort);
        IAppServices server = new AppServicesProtoProxy(serverIP, serverPort);
        System.out.println("Using proto");



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/loggercontroller-view.fxml"));
        Parent root=loader.load();


        ILoggerController ctrl =
                loader.<LoggerController>getController();

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));

        ctrl.setServer(server, primaryStage);




        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/Views/mainController-view.fxml"));

        Parent croot=cloader.load();


        IMainController chatCtrl =
                cloader.<MainController>getController();
        chatCtrl.setServer(server);
        ctrl.setMainController(chatCtrl);



        primaryStage.show();




    }


}