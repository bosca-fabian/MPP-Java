package ProiectMPP.Network.DTO;

import java.io.Serializable;
import java.util.UUID;

public class EmployeeDTO implements Serializable {
    public UUID id;
    public String username;
    public String password;

    public EmployeeDTO(UUID id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

  
    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
