package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.model.AnnouncementEntity;
import gr.uoa.di.madgik.publicfood.model.ChildrenEntity;
import gr.uoa.di.madgik.publicfood.model.UserHasChildrenEntity;
import gr.uoa.di.madgik.publicfood.model.WeeklyQuestionnaireEntity;
import gr.uoa.di.madgik.publicfood.security.repository.ChildRepository;
import gr.uoa.di.madgik.publicfood.security.repository.WeeklyQuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class WeeklyQuestionnaireController {

    @Autowired
    private WeeklyQuestionnaireRepository weeklyQuestionnaireRepository;

    @RequestMapping(value = "/weeklyQuestionnaire/getQuestionnaires", method = RequestMethod.GET)
    public ResponseEntity<?> getWeeklyQuestionnaires(HttpServletRequest request, @RequestParam String userId, @RequestParam String domain) {


        List<WeeklyQuestionnaireEntity> weeklyQuestionnaires = weeklyQuestionnaireRepository.findByUserIdAndDomain(userId, domain);

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        for(WeeklyQuestionnaireEntity weeklyQuestionnaireEntity : weeklyQuestionnaires){

            if(weeklyQuestionnaireEntity.getCompleted() == 1){
                weeklyQuestionnaireEntity.setStatus(WeeklyQuestionnaireEntity.Status.COMPLETED.getValue());
            }
            else {
                if (weeklyQuestionnaireEntity.getDeadline().compareTo(currentTimestamp) < 0) {

                    weeklyQuestionnaireEntity.setStatus(WeeklyQuestionnaireEntity.Status.MISSED.getValue());
                }
                else{
                    weeklyQuestionnaireEntity.setStatus(WeeklyQuestionnaireEntity.Status.RESPOND.getValue());
                }
            }

        }

        Collections.sort(weeklyQuestionnaires, new Comparator<WeeklyQuestionnaireEntity>() {
            @Override
            public int compare(WeeklyQuestionnaireEntity c1, WeeklyQuestionnaireEntity c2) {
                return c1.getDeadline().compareTo(c2.getDeadline());
            }
        });

        if (weeklyQuestionnaires.size() > 3) {
            List<WeeklyQuestionnaireEntity> lastThreeWeeklyQuestionnaires = new ArrayList<WeeklyQuestionnaireEntity>();
            for (int i = weeklyQuestionnaires.size() - 1; i >= weeklyQuestionnaires.size() - 3; i--) {
                lastThreeWeeklyQuestionnaires.add(weeklyQuestionnaires.get(i));
            }
            return new ResponseEntity<List<WeeklyQuestionnaireEntity>>(lastThreeWeeklyQuestionnaires, HttpStatus.OK);
        }


        return new ResponseEntity<List<WeeklyQuestionnaireEntity>>(weeklyQuestionnaires, HttpStatus.OK);

    }

    @RequestMapping(value = "/weeklyQuestionnaire/insertQuestionnaire", method = RequestMethod.POST)
    public ResponseEntity<?> insertWeeklyQuestionnaire(HttpServletRequest request, @RequestParam int userId, @RequestParam String domain) {


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


        WeeklyQuestionnaireEntity weeklyQuestionnaireEntity = new WeeklyQuestionnaireEntity();
        weeklyQuestionnaireEntity.setCompleted((byte) 0);
        weeklyQuestionnaireEntity.setDomain(domain);
        weeklyQuestionnaireEntity.setUserId(String.valueOf(userId));
        Timestamp timestamp = new Timestamp(date1.getTime().getTime());
        weeklyQuestionnaireEntity.setDeadline(timestamp);

        WeeklyQuestionnaireEntity save = weeklyQuestionnaireRepository.save(weeklyQuestionnaireEntity);


        return new ResponseEntity<WeeklyQuestionnaireEntity>(save, HttpStatus.OK);

    }


    @RequestMapping(value = "/weeklyQuestionnaire/updateQuestionnaire", method = RequestMethod.POST)
    public ResponseEntity<?> updateWeeklyQuestionnaire(HttpServletRequest request, @RequestParam int weeklyQuestionnaireId, @RequestParam String domain) {


        WeeklyQuestionnaireEntity weeklyQuestionnaire = weeklyQuestionnaireRepository.findByIdAndDomain(weeklyQuestionnaireId, domain);

        weeklyQuestionnaire.setCompleted((byte) 1);

        WeeklyQuestionnaireEntity save = weeklyQuestionnaireRepository.save(weeklyQuestionnaire);

        return new ResponseEntity<WeeklyQuestionnaireEntity>(save, HttpStatus.OK);

    }

}
