package gr.uoa.di.madgik;

import java.sql.Timestamp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gr.uoa.di.madgik.service.AuthenticationService;
import gr.uoa.di.madgik.service.OrchestratorService;

@Component
public class ScheduledTasks {
	
	@Value("${authorization.server}")
	private  String url;
	@Value("${authorization.server.authenticate}")
	private  String authenticate;
	@Value("${admin.username}")
	private  String username;
	@Value("${admin.password}")
	private  String password;
	
	Timestamp date  = new Timestamp(1431822000);
    
    private final OrchestratorService orchestratorService;

    public ScheduledTasks(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    
    @Scheduled(fixedRate = 1000 * 60 * 1)
    public void reportCurrentTime() throws Exception {
    	
    	long start = System.currentTimeMillis();
    	String result = AuthenticationService.authenticate(url+authenticate, username, password);
        
        JSONObject obj = new JSONObject(result);
        String token = obj.getString("token");
  
        System.out.println(token);
        
        String page1 = orchestratorService.findDatasetsForUpdate(token, date);
        
        date = new Timestamp(start);

    }

}
