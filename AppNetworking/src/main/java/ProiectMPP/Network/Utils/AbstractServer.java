package ProiectMPP.Network.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public abstract class AbstractServer {
    private int port;
    private ServerSocket server=null;
    public AbstractServer(int port){
              this.port=port;
    }

    public void start() throws AppServerException {
        try{
            server=new ServerSocket(port);
            while(true){
                System.out.println("Waiting for clients ...");
                Socket client=server.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new AppServerException("Starting server errror ",e);
        }finally {
            stop();
        }
    }

    protected abstract  void processRequest(Socket client);
    public void stop() throws AppServerException {
        try {
            server.close();
        } catch (IOException e) {
            throw new AppServerException("Closing server error ", e);
        }
    }
}
