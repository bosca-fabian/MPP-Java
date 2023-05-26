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

import java.util.List;
import java.util.UUID;



public class AppClientRpcWorker implements Runnable, IAppObserver {
    private IAppServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AppClientRpcWorker(IAppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
  //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        switch(request.type()) {
            case LOGIN -> {
                System.out.println("Login request ..." + request.type());
                EmployeeDTO employeeDTO = (EmployeeDTO) request.data();
                Employee employee = DTOUtils.getFromDTO(employeeDTO);
                try {
                    server.login(employee, this);
                    return okResponse;
                } catch (AppException e) {
                    connected = false;
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            case LOGOUT ->  {
                System.out.println("Logout request");
                EmployeeDTO employeeDTO = (EmployeeDTO) request.data();
                Employee employee = DTOUtils.getFromDTO(employeeDTO);
                try {
                    server.logout(employee);
                    connected = false;
                    return okResponse;

                } catch (AppException e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            case SEND_CHILD ->  {
                System.out.println("SendChildRequest ...");

                ChildTrialsDTO childTrialsDTO = (ChildTrialsDTO) request.data();

                try {
                    server.sendChild(DTOUtils.getChildFromChildTrialsDTO(childTrialsDTO),
                            DTOUtils.getTrialsFromChildTrialsDTO(childTrialsDTO));
                    return okResponse;
                } catch (AppException e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            case GET_ALL_CHILDREN -> {
                System.out.println("Get children Request...");
                try{
                    List<Child> allChildren = server.getAllChildren();
                    List<ChildDTO> childrenDTO = allChildren.stream().map(DTOUtils::getDTO).toList();
                    return new Response.Builder().type(ResponseType.SEND_ALL_CHILDREN).data(childrenDTO).build();
                } catch (AppException e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

            case GET_ALL_TRIALS -> {
                System.out.println("Get trials Request...");
                try{
                    List<Trial> allTrials = server.getAllTrials();
                    List<TrialDTO> trialsTDO = allTrials.stream().map(DTOUtils::getDTO).toList();
                    return new Response.Builder().type(ResponseType.SEND_ALL_TRIALS).data(trialsTDO).build();
                } catch (AppException e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            case GET_CHILD_TRIALS -> {
                System.out.println("Get child's trials request");
                try {
                    ChildDTO childDTO = (ChildDTO) request.data();
                    Child child = DTOUtils.getFromDTO(childDTO);
                    List<UUID> childTrials = server.getChildTrials(child);
                    return new Response.Builder().type(ResponseType.SEND_CHILD_TRIALS).data(childTrials).build();
                }catch (AppException e){
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
        }
        return response;

    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void childReceived(Child child, List<Trial> trials) throws AppException {
        ChildTrialsDTO childTrialsDTO = DTOUtils.getDTO(child, trials);
        Response resp=new Response.Builder().type(ResponseType.NEW_CHILD).data(childTrialsDTO).build();
        System.out.println("Child received  "+ child);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new AppException("Sending error: "+e);
        }
    }
}
