package RestStart;

import AppRestServices.ServiceException;
import ProiectMPP.Model.Trial;
import RestClient.TrialClient;
import org.springframework.web.client.RestClientException;


public class StartRestClient {
    private final static TrialClient trialsClient=new TrialClient();
    public static void main(String[] args) {
        Trial trialT=new Trial(1500,"trialtestacum","trialdescacum");
        Trial trialTUpdates=new Trial(1500,"trialtestacum2","trialdescacum2");
        trialTUpdates.setId(trialT.getId());
        try{

            show(()-> System.out.println(trialsClient.create(trialT)));
            show(()->{
                Trial[] res=trialsClient.getAll();
                for(Trial u:res){
                    System.out.println(u.getId()+": "+u.getTrialName());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        show(()-> System.out.println(trialsClient.getById(trialT.getId().toString())));

        show(()-> trialsClient.update(trialTUpdates));

        show(()-> System.out.println(trialsClient.getById(trialT.getId().toString())));

        show(()-> trialsClient.delete("1860f06c-bc3b-4cfc-a88f-4351a59e6120"));

        show(trialsClient::getSize);
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
