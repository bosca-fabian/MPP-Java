package ProiectMPP.Network.Utils;


public class AppServerException extends Exception{
    public AppServerException() {
        super();
    }

    public AppServerException(String message) {
        super(message);
    }

    public AppServerException(String message, Throwable cause) {
        super(message, cause);    
    }
}
