package ProiectMPP.Network.RpcProtocols;


import ProiectMPP.Model.Child;
import ProiectMPP.Model.Employee;
import ProiectMPP.Model.Trial;
import ProiectMPP.Network.DTO.*;
import ProiectMPP.Services.AppException;
import ProiectMPP.Services.IAppServices;
import ProiectMPP.Services.IAppObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class AppServicesRpcProxy implements IAppServices {
    private final String host;
    private final int port;

    private IAppObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AppServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<>();
    }

    public void login(Employee employee, IAppObserver client) throws AppException {

        initializeConnection();
        EmployeeDTO employeeDTO= DTOUtils.getDTO(employee);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(employeeDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
    }

    @Override
    public void sendChild(Child child, List<Trial> trialList) throws AppException {
        ChildTrialsDTO mdto= DTOUtils.getDTO(child, trialList);
        Request req=new Request.Builder().type(RequestType.SEND_CHILD).data(mdto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AppException(err);
        }
    }

    public void logout(Employee employee) throws AppException {
        EmployeeDTO udto= DTOUtils.getDTO(employee);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AppException(err);
        }
    }

    public List<Child> getAllChildren() throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_CHILDREN).build();
        sendRequest(req);
        Response response = readResponse();
//        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AppException(err);
        }
        List<Child> children = new ArrayList<>();
        List<ChildDTO> readChildren = (List<ChildDTO>) response.data();
        for (ChildDTO childDTO : readChildren
                ) {
            children.add(DTOUtils.getFromDTO(childDTO));
        }
        return children;
    }

    public List<Trial> getAllTrials() throws AppException{
        Request req = new Request.Builder().type(RequestType.GET_ALL_TRIALS).build();
        sendRequest(req);
        Response response = readResponse();
//        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new AppException(err);
        }
        List<Trial> trials = new ArrayList<>();
        List<TrialDTO> readTrials = (List<TrialDTO>) response.data();
        for (TrialDTO trialDTO : readTrials
        ) {
            trials.add(DTOUtils.getFromDTO(trialDTO));
        }
        return trials;
    }

    @Override
    public List<UUID> getChildTrials(Child child) throws AppException {
        Request req = new Request.Builder().type(RequestType.GET_CHILD_TRIALS).data(DTOUtils.getDTO(child)).build();
        sendRequest(req);
        Response response = readResponse();
        return (List<UUID>) response.data();
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

    private void sendRequest(Request request)throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object "+e);
        }

    }

    private Response readResponse() {
        Response response=null;
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
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
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


    private void handleUpdate(Response response){
        if (response.type()== ResponseType.NEW_CHILD){
            Child child= DTOUtils.getChildFromChildTrialsDTO((ChildTrialsDTO) response.data());
            List<Trial> trials= DTOUtils.getTrialsFromChildTrialsDTO((ChildTrialsDTO) response.data());
            try {
                client.childReceived(child, trials);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.NEW_CHILD;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
