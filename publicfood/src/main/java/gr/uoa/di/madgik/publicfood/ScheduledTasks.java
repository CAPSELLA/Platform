package gr.uoa.di.madgik.publicfood;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import gr.uoa.di.madgik.publicfood.model.UserEntity;
import gr.uoa.di.madgik.publicfood.model.WeeklyQuestionnaireEntity;
import gr.uoa.di.madgik.publicfood.security.repository.UserRepository;
import gr.uoa.di.madgik.publicfood.security.repository.WeeklyQuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
	
//	@Value("${authorization.server}")
//	private  String url;
//	@Value("${authorization.server.authenticate}")
//	private  String authenticate;
//	@Value("${admin.username}")
//	private  String username;
//	@Value("${admin.password}")
//	private  String password;
//
//	Timestamp date  = new Timestamp(1431822000);
//
//    private final OrchestratorService orchestratorService;
//
//    public ScheduledTasks(OrchestratorService orchestratorService) {
//        this.orchestratorService = orchestratorService;
//    }



    @Autowired
    UserRepository userRepository;
    @Autowired
    WeeklyQuestionnaireRepository weeklyQuestionnaireRepository;

    
    @Scheduled(cron = "0 0 0 * * MON")
    public void pingDatabases() throws Exception {



        List<UserEntity> allUsers = userRepository.findAll();
        System.out.println("Public Food database ping, total users: " + allUsers.size());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar date1 = Calendar.getInstance();

        date1.set(Calendar.HOUR, 0);
        date1.set(Calendar.MINUTE, 0);
        date1.set(Calendar.SECOND, 0);
        date1.set(Calendar.HOUR_OF_DAY, 0);

        if(date1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            date1.add(Calendar.DATE, 1);
        }
        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            date1.add(Calendar.DATE, 1);
        }

        System.out.println(date1.getTime());


        for(UserEntity u : allUsers){

            WeeklyQuestionnaireEntity weeklyQuestionnaireEntity = new WeeklyQuestionnaireEntity();
            weeklyQuestionnaireEntity.setCompleted((byte) 0);
            weeklyQuestionnaireEntity.setDomain(u.getDomainName());
            weeklyQuestionnaireEntity.setUserId(String.valueOf(u.getId()));
            Timestamp timestamp = new Timestamp(date1.getTime().getTime());
            weeklyQuestionnaireEntity.setDeadline(timestamp);

            WeeklyQuestionnaireEntity save = weeklyQuestionnaireRepository.save(weeklyQuestionnaireEntity);

        }

    }

}
