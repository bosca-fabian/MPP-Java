package ProiectMPP.Network.ProtobufProtocols;

import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;

import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppObserver;
import ProiectMPP.Services.IAppServices;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppServicesProtoProxy implements IAppServices {
    private final String host;
    private final int port;

    private IAppObserver client;

    private InputStream input;

    private OutputStream output;

    private Socket connection;

    private BlockingQueue<AppProtobuf.Respone> qresponses;

    private volatile boolean finished;

    public AppServicesProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<>();
    }

    public void login(Employee employee, IAppObserver client) throws AppException {

        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(employee));
        AppProtobuf.Respone response=readResponse();
        if (response.getType() == AppProtobuf.Respone.Type.OK){
            this.client=client;
            return;
        }
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            closeConnection();
            throw new AppException(err);
        }
    }

    @Override
    public void sendChild(Child child, List<Trial> trialList) throws AppException {
        AppProtobuf.Request req = ProtoUtils.createSendChildRequest(child, trialList);
        sendRequest(req);
        AppProtobuf.Respone response=readResponse();
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            throw new AppException(err);
        }
    }

    public void logout(Employee employee) throws AppException {
        AppProtobuf.Request req = ProtoUtils.createLogoutRequest(employee);
        sendRequest(req);
        AppProtobuf.Respone response=readResponse();
        closeConnection();
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            throw new AppException(err);
        }
    }

    public List<Child> getAllChildren() throws AppException {
        AppProtobuf.Request req = ProtoUtils.createGetAllChildrenRequest();
        sendRequest(req);
        AppProtobuf.Respone response = readResponse();
//        closeConnection();
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            throw new AppException(err);
        }
        return ProtoUtils.getChildrenFromResponse(response);
    }

    public List<Trial> getAllTrials() throws AppException{
        AppProtobuf.Request req = ProtoUtils.createGetAllTrialsRequest();
        sendRequest(req);
        AppProtobuf.Respone response = readResponse();
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            throw new AppException(err);
        }

        return ProtoUtils.getTrialsFromResponse(response);
    }

    @Override
    public List<UUID> getChildTrials(Child child) throws AppException {
        AppProtobuf.Request req = ProtoUtils.createGetChildSTrialsRequest(child);
        sendRequest(req);
        AppProtobuf.Respone response = readResponse();
        if (response.getType() == AppProtobuf.Respone.Type.ERROR){
            String err=response.getError();
            throw new AppException(err);
        }
        return ProtoUtils.getChildsTrialsFromResponse(response);
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(AppProtobuf.Request request)throws AppException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new AppException("Error sending object "+e);
        }

    }

    private AppProtobuf.Respone readResponse() {
        AppProtobuf.Respone response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(AppProtobuf.Respone response){
        if (response.getType() == AppProtobuf.Respone.Type.NEW_CHILD){
            Child child= ProtoUtils.getChildFromResponse(response);
            List<Trial> trials= ProtoUtils.getTrialListFromResponse(response);
            try {
                client.childReceived(child, trials);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdateResponse(AppProtobuf.Respone.Type type){
        return type == AppProtobuf.Respone.Type.NEW_CHILD;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    AppProtobuf.Respone response=AppProtobuf.Respone.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

}
