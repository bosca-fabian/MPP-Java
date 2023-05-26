package RestClient;

import AppRestServices.ServiceException;
import ProiectMPP.Model.Trial;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Callable;

public class TrialClient
{
    public static final String URL = "http://localhost:8080/app/trials/";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Trial[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Trial[].class));
    }

    public Trial getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Trial.class));
    }

    public Trial create(Trial trial) {
        return execute(() -> restTemplate.postForObject(URL, trial, Trial.class));
    }

    public void update(Trial trial) {
        execute(() -> {
            restTemplate.put(URL, trial);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

    public int getSize(){
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, "size"), int.class));
    }

}