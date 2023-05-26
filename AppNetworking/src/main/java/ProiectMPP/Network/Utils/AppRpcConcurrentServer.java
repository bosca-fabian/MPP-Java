package ProiectMPP.Network.Utils;



import ProiectMPP.Network.RpcProtocols.AppClientRpcWorker;
import ProiectMPP.Services.IAppServices;

import java.net.Socket;


public class AppRpcConcurrentServer extends AbsConcurrentServer {
    private IAppServices chatServer;
    public AppRpcConcurrentServer(int port, IAppServices chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- AppRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
         AppClientRpcWorker worker=new AppClientRpcWorker(chatServer, client);
//        AppClientRpcReflectionWorker worker=new AppClientRpcReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
