package gr.uoa.di.madgik.publicfood.security.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import gr.uoa.di.madgik.publicfood.Utilities.BMICalculation;
import gr.uoa.di.madgik.publicfood.Utilities.StatisticsResults;
import gr.uoa.di.madgik.publicfood.facades.Dataset;
import gr.uoa.di.madgik.publicfood.facades.DatasetTopPercentage;
import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.SurveyEntity;
import gr.uoa.di.madgik.publicfood.limesurvey.limesurveyDAO.SurveyAnswers;
import gr.uoa.di.madgik.publicfood.limesurvey.utilities.SurveyStrings;
import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.model.enums.BMIClasses;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import gr.uoa.di.madgik.publicfood.security.repository.*;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@RestController
@Transactional
public class ChartsController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserHasChildrenRepository userHasChildrenRepository;
    @Autowired
    ChildRepository childRepository;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    CookingCenterRepository cookingCenterRepository;
    @Autowired
    PostCodeRepository postCodeRepository;
    @Autowired
    AreaRepository areaRepository;
    @Value("${json.server}")
    private String mongoUrl;

    @Value("${json.server.upload.json.file}")
    private String upload;

    @Value("${json.server.update.json.file}")
    private String update;

    @Value("${json.server.delete.json.file}")
    private String delete;

    @Value("${datasets.topPercentage.ByPostCode}")
    private String topPercentageByPostCodeUUID;

    @Value("${datasets.topPercentage.ByArea}")
    private String topPercentageByAreaUUID;

    @Value("${datasets.topPercentage.BySchool}")
    private String topPercentageBySchoolUUID;

    @Value("${datasets.topPercentage.ByCity}")
    private String topPercentageByCityUUID;

//    @RequestMapping(value = "/charts/test", method = RequestMethod.GET)
//    public ResponseEntity<?> getChildAllergens(HttpServletRequest request, @RequestParam String token) {
//
//        List<SurveyEntity> answers = surveyRepository.findByToken(token);
//
//        SurveyEntity surveyEntity = SurveyAnswers.lastValidAnswer(answers);
//
//        return new ResponseEntity<SurveyEntity>(surveyEntity, HttpStatus.OK);
//    }

    @RequestMapping(value = "/charts2/children/breakfastAtHome", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenHomeBreakfast(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.breakfastAtHome(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/breakfastFrequency", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenBreakfastFrequency(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.breakfastFrequency(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }

        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/breakfastStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenBreakfasStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.breakfastPopularIngretients(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/chiildren/typesOfPhysicalActivities", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenPhysicalActivities(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.physicalActivities(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/exerciseStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenExerciseStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.excerciseStatistics(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/snacksStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenSnacksStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.snacksStatistics(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String , Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/snacksBetweenMeals", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.snackBetweenMeals(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/children/snacksBetweenMealsTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentageChildrenWhichEatSnacksBetweenMeals(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        int c;
        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;
        if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(SchoolEntity s : schools){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(s.getId()));
                Map<String, Double> results = SurveyAnswers.snackBetweenMeals(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(s.getId(), s.getName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);

            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                statisticsResults.size();
                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(PostCodeEntity postCode : postCodes){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(postCode.getIdPostCode()));
                Map<String, Double> results = SurveyAnswers.snackBetweenMeals(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(postCode.getIdPostCode(), postCode.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            List<AreaEntity> areas = areaRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(AreaEntity area : areas){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(area.getIdarea()));
                Map<String, Double> results = SurveyAnswers.snackBetweenMeals(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(area.getIdarea(), area.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/charts2/children/EatFruitProvidedBySchoolTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentageChildrenWhichEatFruitProvidedBySchool(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        int c;
        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;
        if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            long startTime = System.currentTimeMillis();

            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(SchoolEntity s : schools){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(s.getId()));
                Map<String, Double> results = SurveyAnswers.fruitProvidedBySchool(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(s.getId(), s.getName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                statisticsResults.size();
                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(PostCodeEntity postCode : postCodes){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(postCode.getIdPostCode()));
                Map<String, Double> results = SurveyAnswers.fruitProvidedBySchool(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(postCode.getIdPostCode(), postCode.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            List<AreaEntity> areas = areaRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(AreaEntity area : areas){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(area.getIdarea()));
                Map<String, Double> results = SurveyAnswers.fruitProvidedBySchool(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(area.getIdarea(), area.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/charts2/children/EatFruitProvidedBySchool", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.fruitProvidedBySchool(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/parents/believeThatOrganicFoodIsExpensive", method = RequestMethod.GET)
    public ResponseEntity<?> organicFruitsOrVegetablesExpensive(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.expensiveOrganicFruitsAndVegetables(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/parents/fruitsAreAvailableHome", method = RequestMethod.GET)
    public ResponseEntity<?> fruitsAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.fruitAvailableAtHome(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/parents/fruitsAreAvailableHomeTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagefruitsAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        int c;
        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;
        if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(SchoolEntity s : schools){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(s.getId()));
                Map<String, Double> results = SurveyAnswers.fruitAvailableAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(s.getId(), s.getName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                statisticsResults.size();
                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(PostCodeEntity postCode : postCodes){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(postCode.getIdPostCode()));
                Map<String, Double> results = SurveyAnswers.fruitAvailableAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(postCode.getIdPostCode(), postCode.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            List<AreaEntity> areas = areaRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(AreaEntity area : areas){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(area.getIdarea()));
                Map<String, Double> results = SurveyAnswers.fruitAvailableAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(area.getIdarea(), area.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/charts2/parents/smoothiesAreAvailableHome", method = RequestMethod.GET)
    public ResponseEntity<?> smoothiesAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.juicesAndFruitAtHome(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/parents/smoothies/smoothiesAreAvailableHomeTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagesmoothiesAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        int c;
        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;
        if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(SchoolEntity s : schools){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(s.getId()));
                Map<String, Double> results = SurveyAnswers.juicesAndFruitAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(s.getId(), s.getName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                statisticsResults.size();
                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(PostCodeEntity postCode : postCodes){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(postCode.getIdPostCode()));
                Map<String, Double> results = SurveyAnswers.juicesAndFruitAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(postCode.getIdPostCode(), postCode.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            List<AreaEntity> areas = areaRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(AreaEntity area : areas){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(area.getIdarea()));
                Map<String, Double> results = SurveyAnswers.juicesAndFruitAtHome(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(area.getIdarea(), area.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/charts2/parents/doNotHaveTimeToCookVegetables", method = RequestMethod.GET)
    public ResponseEntity<?> dontHaveTimeToCookVegetables(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        List<SurveyEntity> surveyEntities;
        surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, criteriaValue);

        Map<String, Double> results = SurveyAnswers.dontCookVegetables(surveyEntities);
        if(results == null){
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", "No available answers");
            return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/parents/doNotHaveTimeToCookVegetablesTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagedontHaveTimeToCookVegetables(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        int c;
        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;
        if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(SchoolEntity s : schools){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(s.getId()));
                Map<String, Double> results = SurveyAnswers.dontCookVegetables(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(s.getId(), s.getName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(PostCodeEntity postCode : postCodes){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(postCode.getIdPostCode()));
                Map<String, Double> results = SurveyAnswers.dontCookVegetables(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(postCode.getIdPostCode(), postCode.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }
        else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            List<AreaEntity> areas = areaRepository.findByDomainName(domain);
            List<StatisticsResults> statisticsResults = new ArrayList<StatisticsResults>();
            for(AreaEntity area : areas){
                List<SurveyEntity> surveyEntities;
                surveyEntities = SurveyAnswers.getSurveyAnswers(userRepository, userHasChildrenRepository, childRepository, schoolRepository, cookingCenterRepository, criteria, domain, String.valueOf(area.getIdarea()));
                Map<String, Double> results = SurveyAnswers.dontCookVegetables(surveyEntities);
                if(results != null){
                    double percent = results.get(ResponseKeys.ChartsController_Total_Percentage);
                    StatisticsResults statisticsResults1 = new StatisticsResults(area.getIdarea(), area.getDomainName(), percent);
                    statisticsResults.add(statisticsResults1);
                }
            }
            if(statisticsResults.isEmpty())
            {
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", "No available answers");
                return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
            }
            else{
                Collections.sort(statisticsResults, new Comparator<StatisticsResults>() {
                    @Override
                    public int compare(StatisticsResults c1, StatisticsResults c2) {
                        return Double.compare(c1.getResult(), c2.getResult());
                    }
                });

                return new ResponseEntity<StatisticsResults>( statisticsResults.get(statisticsResults.size() -1), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/charts/bmi/statistics", method = RequestMethod.GET)
    public ResponseEntity<?> getBMIStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {
        int c;
        double[] bmiPercentages = new double[4];
        int[] bmiClassCounter = new int[4];

        int childCount = 0;
        Map<String, String> responseMap = new HashMap<String, String>();

        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;


        List<ChildrenEntity> totalChildren = new ArrayList<ChildrenEntity>();

        if (SurveyEntity.Criteria.CITY.getValue() == c) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);
            for (SchoolEntity s : schools) {
                List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, s.getId());
                for(ChildrenEntity childrenEntity : childrens){
                    totalChildren.add(childrenEntity);
                }
            }

        } else if (SurveyEntity.Criteria.SCHOOL.getValue() == c) {
            int schoolId = Integer.parseInt(criteriaValue);
            totalChildren = childRepository.findByDomainNameAndSchoolId(domain, schoolId);

        } else if (SurveyEntity.Criteria.POSTCODE.getValue() == c) {
            int postCodeId = Integer.parseInt(criteriaValue);
            List<SchoolEntity> schools = schoolRepository.findByDomainNameAndPostCodeIdPostCode(domain, postCodeId);
            for (SchoolEntity s : schools) {
                List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, s.getId());
                for(ChildrenEntity childrenEntity : childrens){
                    totalChildren.add(childrenEntity);
                }
            }

        } else if (SurveyEntity.Criteria.AREA.getValue() == c) {
            int area = Integer.parseInt(criteriaValue);
            List<CookingCenterEntity> cookingCenterEntities = cookingCenterRepository.findByDomainNameAndArea(domain, area);
            for(CookingCenterEntity cookingCenterEntity : cookingCenterEntities) {
                List<SchoolEntity> schools = schoolRepository.findByDomainNameAndCookingCenterId(domain, cookingCenterEntity.getId());
                    for (SchoolEntity s : schools) {
                        List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, s.getId());
                        for(ChildrenEntity childrenEntity : childrens){
                            totalChildren.add(childrenEntity);
                        }
                    }
                }

        }



        for (ChildrenEntity child : totalChildren) {
            Map<String, String> result = BMICalculation.calculateBMI(child);
            int bmiClass = Integer.parseInt(result.get(ResponseKeys.ChildController_BMI_Class));
            if(bmiClass == BMIClasses.Underweight.getValue()){

                bmiClassCounter[0]++;
                childCount++;
            }
            else if(bmiClass == BMIClasses.Healthy.getValue()){

                bmiClassCounter[1]++;
                childCount++;
            }
            else if(bmiClass == BMIClasses.Overweight.getValue()){

                bmiClassCounter[2]++;
                childCount++;
            }
            else if(bmiClass == BMIClasses.Obese.getValue()){

                bmiClassCounter[3]++;
                childCount++;
            }
        }

        if (childCount > 0) {
            for(int i=0 ;i< 3; i++){
                bmiPercentages[i] = ((double) bmiClassCounter[i] / (double) childCount) * 100;
                bmiPercentages[i] = SurveyAnswers.round(bmiPercentages[i], 0);

            }
            System.out.println(childCount);
            responseMap.put(ResponseKeys.ChildController_BMI_Class_Underweight, String.valueOf(bmiPercentages[0]));
            responseMap.put(ResponseKeys.ChildController_BMI_Class_Healthy, String.valueOf(bmiPercentages[1]));
            responseMap.put(ResponseKeys.ChildController_BMI_Class_Overweight, String.valueOf(bmiPercentages[2]));
            responseMap.put(ResponseKeys.ChildController_BMI_Class_Obese, String.valueOf(bmiPercentages[3]));

        }
        else {
            responseMap.put("error", "No available answers");
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

    }

    @RequestMapping(value = "/charts2/datasetsByCity", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsByCity(@RequestParam String domain){


        List<Dataset> datasets = new ArrayList<>();

        Dataset dataset = new Dataset();
        dataset.setName(domain);

        ResponseEntity<?> getChildrenSnacksStatistics = getChildrenSnacksStatistics(null, domain, "1", String.valueOf(1));
        System.out.println(getChildrenSnacksStatistics.getBody());
        Map<String, Double> getChildrenSnacksStatisticsResponse = (Map<String, Double>) getChildrenSnacksStatistics.getBody();

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables);
            dataset.setFavorite_snacks_Fruit_Or_Vegetables(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar);
            dataset.setFavorite_snacks_Savoury_Snack_Bar(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar);
            dataset.setFavorite_snacks_Sweet_Snack_Bar(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Nuts)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Nuts);
            dataset.setFavorite_snacks_Nuts(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Chocolate)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Chocolate);
            dataset.setFavorite_snacks_Chocolate(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Candy)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Candy);
            dataset.setFavorite_snacks_Candy(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits);
            dataset.setFavorite_snacks_Cakes_And_Biscuits(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat);
            dataset.setFavorite_snacks_Bread_And_Cured_Meat(result);
        }

        if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Other)){
            Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Other);
            dataset.setFavorite_snacks_Other(result);
        }


        ResponseEntity<?> excerciseStatistics = getChildrenExerciseStatistics(null, domain, "1", String.valueOf(1));
        System.out.println(excerciseStatistics.getBody());
        Map<String, Double> excerciseStatisticsResponse = (Map<String, Double>) excerciseStatistics.getBody();

        if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_0_1_hours)){
            Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_0_1_hours);
            dataset.setExerciseHours_0_1_hours(result);
        }

        if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_1_3_hours)){
            Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_1_3_hours);
            dataset.setExerciseHours_1_3_hours(result);
        }

        if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_3_7_hours)){
            Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_3_7_hours);
            dataset.setExerciseHours_3_7_hours(result);
        }

        if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours)){
            Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours);
            dataset.setExerciseHours_moreThan_7_hours(result);
        }

        ResponseEntity<?> breakfastStatistics = getChildrenBreakfasStatistics(null, domain, "1", String.valueOf(1));
        System.out.println(breakfastStatistics.getBody());
        Map<String, Double> breakfastStatisticsResponse = (Map<String, Double>) breakfastStatistics.getBody();

        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_milk_yogurt)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_milk_yogurt);
            dataset.setBreakfastPopularIngredients_milk_yogurt(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_biscuits_cake)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_biscuits_cake);
            dataset.setBreakfastPopularIngredients_biscuits_cake(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_bread)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_bread);
            dataset.setBreakfastPopularIngredients_bread(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_snacks)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_snacks);
            dataset.setBreakfastPopularIngredients_snacks(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_cereal)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_cereal);
            dataset.setBreakfastPopularIngredients_cereal(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_dried_fruits)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_dried_fruits);
            dataset.setBreakfastPopularIngredients_dried_fruits(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_tea)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_tea);
            dataset.setBreakfastPopularIngredients_tea(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_fruit_juice)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_fruit_juice);
            dataset.setBreakfastPopularIngredients_fruit_juice(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_coffee)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_coffee);
            dataset.setBreakfastPopularIngredients_coffee(result);
        }
        if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_eggs)){
            Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_eggs);
            dataset.setBreakfastPopularIngredients_eggs(result);
        }

        ResponseEntity<?> getChildrenPhysicalActivities = getChildrenPhysicalActivities(null, domain, "1", String.valueOf(1));
        System.out.println(getChildrenPhysicalActivities.getBody());
        Map<String, Double> getChildrenPhysicalActivitiesResponse = (Map<String, Double>) getChildrenPhysicalActivities.getBody();

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Athletics)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Athletics);
            dataset.setPhysicalActivities_Athletics(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Basketball)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Basketball);
            dataset.setPhysicalActivities_Basketball(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Cycling)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Cycling);
            dataset.setPhysicalActivities_Cycling(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Dancing)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Dancing);
            dataset.setPhysicalActivities_Dancing(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Gymnastics)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Gymnastics);
            dataset.setPhysicalActivities_Gymnastics(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Soccer)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Soccer);
            dataset.setPhysicalActivities_Soccer(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Swimming)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Swimming);
            dataset.setPhysicalActivities_Swimming(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Tennis)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Tennis);
            dataset.setPhysicalActivities_Tennis(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Volleyball)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Volleyball);
            dataset.setPhysicalActivities_Volleyball(result);
        }

        if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Other)){
            Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Other);
            dataset.setPhysicalActivities_Other(result);
        }





        ResponseEntity<?> dontHaveTimeToCookVegetables = dontHaveTimeToCookVegetables(null, domain, "1", String.valueOf(1));
        System.out.println(dontHaveTimeToCookVegetables.getBody());
        Map<String, Double> dontHaveTimeToCookVegetablesResponse = (Map<String, Double>) dontHaveTimeToCookVegetables.getBody();

        if(dontHaveTimeToCookVegetablesResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = dontHaveTimeToCookVegetablesResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setParentsDontHaveTimeToCookVegetables(result);
        }

        ResponseEntity<?> childrenHomeBreakfast = getChildrenHomeBreakfast(null, domain, "1", String.valueOf(1));
        Map<String, Double> childrenHomeBreakfastResponse =  (Map<String, Double>) (childrenHomeBreakfast.getBody());

        if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setChildrenEatBreakfastAtHome(result);
        }
        if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Male_Percentage)){
            Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Male_Percentage);
            dataset.setChildrenEatBreakfastAtHomeMale(result);
        }
        if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Female_Percentage)){
            Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Female_Percentage);
            dataset.setChildrenEatBreakfastAtHomeFemale(result);
        }

        ResponseEntity<?> getChildrenBreakfastFrequency = getChildrenBreakfastFrequency(null, domain, "1", String.valueOf(1));
        Map<String, Double> getChildrenBreakfastFrequencyResponse =  (Map<String, Double>) (getChildrenBreakfastFrequency.getBody());

        if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_EveryDay_Percentage)){
            Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_EveryDay_Percentage);
            dataset.setBreakfastFrequency(result);
        }

        if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_Never_Percentage)){
            Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_Never_Percentage);
            dataset.setBreakfastFrequencyNever(result);
        }

        ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals = getChildrenWhichEatSnacksBetweenMeals(null, domain, "1", String.valueOf(1));

        Map<String, Double> getChildrenWhichEatSnacksBetweenMealsResponse =  (Map<String, Double>) (getChildrenWhichEatSnacksBetweenMeals.getBody());

        if(getChildrenWhichEatSnacksBetweenMealsResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = getChildrenWhichEatSnacksBetweenMealsResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setChildrenWhichEatSnacksBetweenMeals(result);
        }


        ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool = getChildrenWhichEatFruitProvidedBySchool(null, domain, "1", String.valueOf(1));
        Map<String, Double> getChildrenWhichEatFruitProvidedBySchoolResponse =  (Map<String, Double>) (getChildrenWhichEatFruitProvidedBySchool.getBody());

        if(getChildrenWhichEatFruitProvidedBySchoolResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = getChildrenWhichEatFruitProvidedBySchoolResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setChildrenWhichEatFruitProvidedBySchool(result);
        }


        ResponseEntity<?> organicFruitsOrVegetablesExpensive = organicFruitsOrVegetablesExpensive(null, domain, "1", String.valueOf(1));
        Map<String, Double> organicFruitsOrVegetablesExpensiveResponse =  (Map<String, Double>) (organicFruitsOrVegetablesExpensive.getBody());


        if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicFood_Percentage)){
            Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicFood_Percentage);
            dataset.setParentsBelieveThatOrganicFoodIsExpensive(result);
        }

        if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicVegetables_Percentage)){
            Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicVegetables_Percentage);
            dataset.setParentsBelieveThatOrganicVegetablesAreExpensive(result);
        }

        ResponseEntity<?> smoothiesAvailableHome = smoothiesAvailableHome(null, domain, "1", String.valueOf(1));
        Map<String, Double> smoothiesAvailableHomeResponse =  (Map<String, Double>) (smoothiesAvailableHome.getBody());

        if(smoothiesAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = smoothiesAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setSmoothiesAreAvailableHome(result);
        }


        ResponseEntity<?> fruitsAvailableHome = fruitsAvailableHome(null, domain, "1", String.valueOf(1));
        Map<String, Double> fruitsAvailableHomeResponse =  (Map<String, Double>) (fruitsAvailableHome.getBody());

        if(fruitsAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
            Double result = fruitsAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
            dataset.setFruitsAreAvailableHome(result);
        }

        datasets.add(dataset);



        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        File convFile = new File("publicFoodCityStatistics.json");
        String json = new Gson().toJson(datasets);

        try {
            if(!convFile.exists())
                convFile.createNewFile();
            else
            {
                convFile.delete();
                convFile.createNewFile();
            }

            FileUtils.writeStringToFile(
                    convFile, json, StandardCharsets.UTF_8, true);
            multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
            multipartMap.add("uuid", topPercentageByCityUUID);
            multipartMap.add("collection", "publicFoodCityStatistics_"+domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mongoUrl + update,
                    request, String.class);
            convFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<List<Dataset>> (datasets, HttpStatus.OK);
    }


    @RequestMapping(value = "/charts2/datasetsByschool", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsBySchool(@RequestParam String domain){


        List<Dataset> datasets = new ArrayList<>();

        List<SchoolEntity> byDomainName = schoolRepository.findByDomainName(domain);

        for(SchoolEntity schoolEntity : byDomainName){



            Dataset dataset = new Dataset();
            dataset.setName(domain);
            dataset.setId(schoolEntity.getId());

            ResponseEntity<?> getChildrenSnacksStatistics = getChildrenSnacksStatistics(null, domain, "0", String.valueOf(schoolEntity.getId()));
            System.out.println(getChildrenSnacksStatistics.getBody());
            Map<String, Double> getChildrenSnacksStatisticsResponse = (Map<String, Double>) getChildrenSnacksStatistics.getBody();

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables);
                dataset.setFavorite_snacks_Fruit_Or_Vegetables(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar);
                dataset.setFavorite_snacks_Savoury_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar);
                dataset.setFavorite_snacks_Sweet_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Nuts)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Nuts);
                dataset.setFavorite_snacks_Nuts(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Chocolate)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Chocolate);
                dataset.setFavorite_snacks_Chocolate(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Candy)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Candy);
                dataset.setFavorite_snacks_Candy(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits);
                dataset.setFavorite_snacks_Cakes_And_Biscuits(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat);
                dataset.setFavorite_snacks_Bread_And_Cured_Meat(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Other)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Other);
                dataset.setFavorite_snacks_Other(result);
            }


            ResponseEntity<?> excerciseStatistics = getChildrenExerciseStatistics(null, domain, "0", String.valueOf(schoolEntity.getId()));
            System.out.println(excerciseStatistics.getBody());
            Map<String, Double> excerciseStatisticsResponse = (Map<String, Double>) excerciseStatistics.getBody();

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_0_1_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_0_1_hours);
                dataset.setExerciseHours_0_1_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_1_3_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_1_3_hours);
                dataset.setExerciseHours_1_3_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_3_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_3_7_hours);
                dataset.setExerciseHours_3_7_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours);
                dataset.setExerciseHours_moreThan_7_hours(result);
            }

            ResponseEntity<?> breakfastStatistics = getChildrenBreakfasStatistics(null, domain, "0", String.valueOf(schoolEntity.getId()));
            System.out.println(breakfastStatistics.getBody());
            Map<String, Double> breakfastStatisticsResponse = (Map<String, Double>) breakfastStatistics.getBody();

            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_milk_yogurt)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_milk_yogurt);
                dataset.setBreakfastPopularIngredients_milk_yogurt(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_biscuits_cake)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_biscuits_cake);
                dataset.setBreakfastPopularIngredients_biscuits_cake(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_bread)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_bread);
                dataset.setBreakfastPopularIngredients_bread(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_snacks)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_snacks);
                dataset.setBreakfastPopularIngredients_snacks(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_cereal)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_cereal);
                dataset.setBreakfastPopularIngredients_cereal(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_dried_fruits)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_dried_fruits);
                dataset.setBreakfastPopularIngredients_dried_fruits(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_tea)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_tea);
                dataset.setBreakfastPopularIngredients_tea(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_fruit_juice)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_fruit_juice);
                dataset.setBreakfastPopularIngredients_fruit_juice(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_coffee)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_coffee);
                dataset.setBreakfastPopularIngredients_coffee(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_eggs)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_eggs);
                dataset.setBreakfastPopularIngredients_eggs(result);
            }

            ResponseEntity<?> getChildrenPhysicalActivities = getChildrenPhysicalActivities(null, domain, "0", String.valueOf(schoolEntity.getId()));
            System.out.println(getChildrenPhysicalActivities.getBody());
            Map<String, Double> getChildrenPhysicalActivitiesResponse = (Map<String, Double>) getChildrenPhysicalActivities.getBody();

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Athletics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Athletics);
                dataset.setPhysicalActivities_Athletics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Basketball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Basketball);
                dataset.setPhysicalActivities_Basketball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Cycling)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Cycling);
                dataset.setPhysicalActivities_Cycling(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Dancing)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Dancing);
                dataset.setPhysicalActivities_Dancing(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Gymnastics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Gymnastics);
                dataset.setPhysicalActivities_Gymnastics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Soccer)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Soccer);
                dataset.setPhysicalActivities_Soccer(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Swimming)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Swimming);
                dataset.setPhysicalActivities_Swimming(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Tennis)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Tennis);
                dataset.setPhysicalActivities_Tennis(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Volleyball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Volleyball);
                dataset.setPhysicalActivities_Volleyball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Other)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Other);
                dataset.setPhysicalActivities_Other(result);
            }





            ResponseEntity<?> dontHaveTimeToCookVegetables = dontHaveTimeToCookVegetables(null, domain, "0", String.valueOf(schoolEntity.getId()));
            System.out.println(dontHaveTimeToCookVegetables.getBody());
            Map<String, Double> dontHaveTimeToCookVegetablesResponse = (Map<String, Double>) dontHaveTimeToCookVegetables.getBody();

            if(dontHaveTimeToCookVegetablesResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = dontHaveTimeToCookVegetablesResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setParentsDontHaveTimeToCookVegetables(result);
            }

            ResponseEntity<?> childrenHomeBreakfast = getChildrenHomeBreakfast(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> childrenHomeBreakfastResponse =  (Map<String, Double>) (childrenHomeBreakfast.getBody());

            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenEatBreakfastAtHome(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Male_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Male_Percentage);
                dataset.setChildrenEatBreakfastAtHomeMale(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Female_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Female_Percentage);
                dataset.setChildrenEatBreakfastAtHomeFemale(result);
            }

            ResponseEntity<?> getChildrenBreakfastFrequency = getChildrenBreakfastFrequency(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> getChildrenBreakfastFrequencyResponse =  (Map<String, Double>) (getChildrenBreakfastFrequency.getBody());

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_EveryDay_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_EveryDay_Percentage);
                dataset.setBreakfastFrequency(result);
            }

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_Never_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_Never_Percentage);
                dataset.setBreakfastFrequencyNever(result);
            }

            ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals = getChildrenWhichEatSnacksBetweenMeals(null, domain, "0", String.valueOf(schoolEntity.getId()));

            Map<String, Double> getChildrenWhichEatSnacksBetweenMealsResponse =  (Map<String, Double>) (getChildrenWhichEatSnacksBetweenMeals.getBody());

            if(getChildrenWhichEatSnacksBetweenMealsResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatSnacksBetweenMealsResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatSnacksBetweenMeals(result);
            }


            ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool = getChildrenWhichEatFruitProvidedBySchool(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> getChildrenWhichEatFruitProvidedBySchoolResponse =  (Map<String, Double>) (getChildrenWhichEatFruitProvidedBySchool.getBody());

            if(getChildrenWhichEatFruitProvidedBySchoolResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatFruitProvidedBySchoolResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatFruitProvidedBySchool(result);
            }


            ResponseEntity<?> organicFruitsOrVegetablesExpensive = organicFruitsOrVegetablesExpensive(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> organicFruitsOrVegetablesExpensiveResponse =  (Map<String, Double>) (organicFruitsOrVegetablesExpensive.getBody());


            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicFood_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicFood_Percentage);
                dataset.setParentsBelieveThatOrganicFoodIsExpensive(result);
            }

            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicVegetables_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicVegetables_Percentage);
                dataset.setParentsBelieveThatOrganicVegetablesAreExpensive(result);
            }

            ResponseEntity<?> smoothiesAvailableHome = smoothiesAvailableHome(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> smoothiesAvailableHomeResponse =  (Map<String, Double>) (smoothiesAvailableHome.getBody());

            if(smoothiesAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = smoothiesAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setSmoothiesAreAvailableHome(result);
            }


            ResponseEntity<?> fruitsAvailableHome = fruitsAvailableHome(null, domain, "0", String.valueOf(schoolEntity.getId()));
            Map<String, Double> fruitsAvailableHomeResponse =  (Map<String, Double>) (fruitsAvailableHome.getBody());

            if(fruitsAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = fruitsAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setFruitsAreAvailableHome(result);
            }

            datasets.add(dataset);


            MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
            File convFile = new File("publicFoodSchoolsStatistics.json");
            String json = new Gson().toJson(datasets);

            try {
                if(!convFile.exists())
                    convFile.createNewFile();
                else
                {
                    convFile.delete();
                    convFile.createNewFile();
                }

                FileUtils.writeStringToFile(
                        convFile, json, StandardCharsets.UTF_8, true);
                multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
                multipartMap.add("uuid", topPercentageBySchoolUUID);
                multipartMap.add("collection", "publicFoodSchoolsStatistics_"+domain);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(
                        mongoUrl + update,
                        request, String.class);
                convFile.delete();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return new ResponseEntity<List<Dataset>> (datasets, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/datasetsByPostCode", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsByPostCode(@RequestParam String domain){


        List<Dataset> datasets = new ArrayList<>();

        List<PostCodeEntity> byDomainName = postCodeRepository.findByDomainName(domain);

        for(PostCodeEntity postCodeEntity : byDomainName){

            Dataset dataset = new Dataset();
            dataset.setName(domain);
            dataset.setId(postCodeEntity.getIdPostCode());

            ResponseEntity<?> getChildrenSnacksStatistics = getChildrenSnacksStatistics(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            System.out.println(getChildrenSnacksStatistics.getBody());
            Map<String, Double> getChildrenSnacksStatisticsResponse = (Map<String, Double>) getChildrenSnacksStatistics.getBody();

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables);
                dataset.setFavorite_snacks_Fruit_Or_Vegetables(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar);
                dataset.setFavorite_snacks_Savoury_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar);
                dataset.setFavorite_snacks_Sweet_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Nuts)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Nuts);
                dataset.setFavorite_snacks_Nuts(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Chocolate)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Chocolate);
                dataset.setFavorite_snacks_Chocolate(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Candy)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Candy);
                dataset.setFavorite_snacks_Candy(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits);
                dataset.setFavorite_snacks_Cakes_And_Biscuits(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat);
                dataset.setFavorite_snacks_Bread_And_Cured_Meat(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Other)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Other);
                dataset.setFavorite_snacks_Other(result);
            }


            ResponseEntity<?> excerciseStatistics = getChildrenExerciseStatistics(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            System.out.println(excerciseStatistics.getBody());
            Map<String, Double> excerciseStatisticsResponse = (Map<String, Double>) excerciseStatistics.getBody();

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_0_1_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_0_1_hours);
                dataset.setExerciseHours_0_1_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_1_3_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_1_3_hours);
                dataset.setExerciseHours_1_3_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_3_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_3_7_hours);
                dataset.setExerciseHours_3_7_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours);
                dataset.setExerciseHours_moreThan_7_hours(result);
            }

            ResponseEntity<?> breakfastStatistics = getChildrenBreakfasStatistics(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            System.out.println(breakfastStatistics.getBody());
            Map<String, Double> breakfastStatisticsResponse = (Map<String, Double>) breakfastStatistics.getBody();

            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_milk_yogurt)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_milk_yogurt);
                dataset.setBreakfastPopularIngredients_milk_yogurt(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_biscuits_cake)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_biscuits_cake);
                dataset.setBreakfastPopularIngredients_biscuits_cake(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_bread)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_bread);
                dataset.setBreakfastPopularIngredients_bread(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_snacks)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_snacks);
                dataset.setBreakfastPopularIngredients_snacks(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_cereal)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_cereal);
                dataset.setBreakfastPopularIngredients_cereal(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_dried_fruits)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_dried_fruits);
                dataset.setBreakfastPopularIngredients_dried_fruits(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_tea)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_tea);
                dataset.setBreakfastPopularIngredients_tea(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_fruit_juice)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_fruit_juice);
                dataset.setBreakfastPopularIngredients_fruit_juice(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_coffee)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_coffee);
                dataset.setBreakfastPopularIngredients_coffee(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_eggs)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_eggs);
                dataset.setBreakfastPopularIngredients_eggs(result);
            }

            ResponseEntity<?> getChildrenPhysicalActivities = getChildrenPhysicalActivities(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            System.out.println(getChildrenPhysicalActivities.getBody());
            Map<String, Double> getChildrenPhysicalActivitiesResponse = (Map<String, Double>) getChildrenPhysicalActivities.getBody();

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Athletics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Athletics);
                dataset.setPhysicalActivities_Athletics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Basketball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Basketball);
                dataset.setPhysicalActivities_Basketball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Cycling)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Cycling);
                dataset.setPhysicalActivities_Cycling(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Dancing)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Dancing);
                dataset.setPhysicalActivities_Dancing(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Gymnastics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Gymnastics);
                dataset.setPhysicalActivities_Gymnastics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Soccer)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Soccer);
                dataset.setPhysicalActivities_Soccer(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Swimming)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Swimming);
                dataset.setPhysicalActivities_Swimming(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Tennis)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Tennis);
                dataset.setPhysicalActivities_Tennis(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Volleyball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Volleyball);
                dataset.setPhysicalActivities_Volleyball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Other)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Other);
                dataset.setPhysicalActivities_Other(result);
            }





            ResponseEntity<?> dontHaveTimeToCookVegetables = dontHaveTimeToCookVegetables(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            System.out.println(dontHaveTimeToCookVegetables.getBody());
            Map<String, Double> dontHaveTimeToCookVegetablesResponse = (Map<String, Double>) dontHaveTimeToCookVegetables.getBody();

            if(dontHaveTimeToCookVegetablesResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = dontHaveTimeToCookVegetablesResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setParentsDontHaveTimeToCookVegetables(result);
            }

            ResponseEntity<?> childrenHomeBreakfast = getChildrenHomeBreakfast(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> childrenHomeBreakfastResponse =  (Map<String, Double>) (childrenHomeBreakfast.getBody());

            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenEatBreakfastAtHome(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Male_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Male_Percentage);
                dataset.setChildrenEatBreakfastAtHomeMale(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Female_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Female_Percentage);
                dataset.setChildrenEatBreakfastAtHomeFemale(result);
            }

            ResponseEntity<?> getChildrenBreakfastFrequency = getChildrenBreakfastFrequency(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> getChildrenBreakfastFrequencyResponse =  (Map<String, Double>) (getChildrenBreakfastFrequency.getBody());

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_EveryDay_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_EveryDay_Percentage);
                dataset.setBreakfastFrequency(result);
            }

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_Never_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_Never_Percentage);
                dataset.setBreakfastFrequencyNever(result);
            }

            ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals = getChildrenWhichEatSnacksBetweenMeals(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));

            Map<String, Double> getChildrenWhichEatSnacksBetweenMealsResponse =  (Map<String, Double>) (getChildrenWhichEatSnacksBetweenMeals.getBody());

            if(getChildrenWhichEatSnacksBetweenMealsResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatSnacksBetweenMealsResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatSnacksBetweenMeals(result);
            }


            ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool = getChildrenWhichEatFruitProvidedBySchool(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> getChildrenWhichEatFruitProvidedBySchoolResponse =  (Map<String, Double>) (getChildrenWhichEatFruitProvidedBySchool.getBody());

            if(getChildrenWhichEatFruitProvidedBySchoolResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatFruitProvidedBySchoolResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatFruitProvidedBySchool(result);
            }


            ResponseEntity<?> organicFruitsOrVegetablesExpensive = organicFruitsOrVegetablesExpensive(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> organicFruitsOrVegetablesExpensiveResponse =  (Map<String, Double>) (organicFruitsOrVegetablesExpensive.getBody());


            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicFood_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicFood_Percentage);
                dataset.setParentsBelieveThatOrganicFoodIsExpensive(result);
            }

            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicVegetables_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicVegetables_Percentage);
                dataset.setParentsBelieveThatOrganicVegetablesAreExpensive(result);
            }

            ResponseEntity<?> smoothiesAvailableHome = smoothiesAvailableHome(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> smoothiesAvailableHomeResponse =  (Map<String, Double>) (smoothiesAvailableHome.getBody());

            if(smoothiesAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = smoothiesAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setSmoothiesAreAvailableHome(result);
            }


            ResponseEntity<?> fruitsAvailableHome = fruitsAvailableHome(null, domain, "3", String.valueOf(postCodeEntity.getIdPostCode()));
            Map<String, Double> fruitsAvailableHomeResponse =  (Map<String, Double>) (fruitsAvailableHome.getBody());

            if(fruitsAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = fruitsAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setFruitsAreAvailableHome(result);
            }

            datasets.add(dataset);


        }

        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        File convFile = new File("publicFoodPostCodeStatistics.json");
        String json = new Gson().toJson(datasets);

        try {
            if(!convFile.exists())
                convFile.createNewFile();
            else
            {
                convFile.delete();
                convFile.createNewFile();
            }

            FileUtils.writeStringToFile(
                    convFile, json, StandardCharsets.UTF_8, true);
            multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
            multipartMap.add("uuid", topPercentageByPostCodeUUID);
            multipartMap.add("collection", "publicFoodPostCodeStatistics_"+domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mongoUrl + update,
                    request, String.class);
            convFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<Dataset>> (datasets, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/datasetsByArea", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsByArea(@RequestParam String domain){


        List<Dataset> datasets = new ArrayList<>();

        List<AreaEntity> byDomainName = areaRepository.findByDomainName(domain);

        for(AreaEntity areaEntity : byDomainName){

            Dataset dataset = new Dataset();
            dataset.setName(domain);
            dataset.setId(areaEntity.getIdarea());

            ResponseEntity<?> getChildrenSnacksStatistics = getChildrenSnacksStatistics(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            System.out.println(getChildrenSnacksStatistics.getBody());
            Map<String, Double> getChildrenSnacksStatisticsResponse = (Map<String, Double>) getChildrenSnacksStatistics.getBody();

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables);
                dataset.setFavorite_snacks_Fruit_Or_Vegetables(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar);
                dataset.setFavorite_snacks_Savoury_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar);
                dataset.setFavorite_snacks_Sweet_Snack_Bar(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Nuts)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Nuts);
                dataset.setFavorite_snacks_Nuts(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Chocolate)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Chocolate);
                dataset.setFavorite_snacks_Chocolate(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Candy)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Candy);
                dataset.setFavorite_snacks_Candy(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits);
                dataset.setFavorite_snacks_Cakes_And_Biscuits(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat);
                dataset.setFavorite_snacks_Bread_And_Cured_Meat(result);
            }

            if(getChildrenSnacksStatisticsResponse.containsKey(ResponseKeys.ChartsController_favorite_snacks_Other)){
                Double result = getChildrenSnacksStatisticsResponse.get(ResponseKeys.ChartsController_favorite_snacks_Other);
                dataset.setFavorite_snacks_Other(result);
            }


            ResponseEntity<?> excerciseStatistics = getChildrenExerciseStatistics(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            System.out.println(excerciseStatistics.getBody());
            Map<String, Double> excerciseStatisticsResponse = (Map<String, Double>) excerciseStatistics.getBody();

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_0_1_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_0_1_hours);
                dataset.setExerciseHours_0_1_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_1_3_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_1_3_hours);
                dataset.setExerciseHours_1_3_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_3_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_3_7_hours);
                dataset.setExerciseHours_3_7_hours(result);
            }

            if(excerciseStatisticsResponse.containsKey(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours)){
                Double result = excerciseStatisticsResponse.get(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours);
                dataset.setExerciseHours_moreThan_7_hours(result);
            }

            ResponseEntity<?> breakfastStatistics = getChildrenBreakfasStatistics(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            System.out.println(breakfastStatistics.getBody());
            Map<String, Double> breakfastStatisticsResponse = (Map<String, Double>) breakfastStatistics.getBody();

            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_milk_yogurt)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_milk_yogurt);
                dataset.setBreakfastPopularIngredients_milk_yogurt(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_biscuits_cake)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_biscuits_cake);
                dataset.setBreakfastPopularIngredients_biscuits_cake(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_bread)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_bread);
                dataset.setBreakfastPopularIngredients_bread(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_snacks)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_snacks);
                dataset.setBreakfastPopularIngredients_snacks(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_cereal)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_cereal);
                dataset.setBreakfastPopularIngredients_cereal(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_dried_fruits)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_dried_fruits);
                dataset.setBreakfastPopularIngredients_dried_fruits(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_tea)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_tea);
                dataset.setBreakfastPopularIngredients_tea(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_fruit_juice)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_fruit_juice);
                dataset.setBreakfastPopularIngredients_fruit_juice(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_coffee)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_coffee);
                dataset.setBreakfastPopularIngredients_coffee(result);
            }
            if(breakfastStatisticsResponse.containsKey(SurveyStrings.breakfastPopularIngredients_eggs)){
                Double result = breakfastStatisticsResponse.get(SurveyStrings.breakfastPopularIngredients_eggs);
                dataset.setBreakfastPopularIngredients_eggs(result);
            }

            ResponseEntity<?> getChildrenPhysicalActivities = getChildrenPhysicalActivities(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            System.out.println(getChildrenPhysicalActivities.getBody());
            Map<String, Double> getChildrenPhysicalActivitiesResponse = (Map<String, Double>) getChildrenPhysicalActivities.getBody();

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Athletics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Athletics);
                dataset.setPhysicalActivities_Athletics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Basketball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Basketball);
                dataset.setPhysicalActivities_Basketball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Cycling)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Cycling);
                dataset.setPhysicalActivities_Cycling(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Dancing)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Dancing);
                dataset.setPhysicalActivities_Dancing(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Gymnastics)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Gymnastics);
                dataset.setPhysicalActivities_Gymnastics(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Soccer)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Soccer);
                dataset.setPhysicalActivities_Soccer(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Swimming)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Swimming);
                dataset.setPhysicalActivities_Swimming(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Tennis)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Tennis);
                dataset.setPhysicalActivities_Tennis(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Volleyball)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Volleyball);
                dataset.setPhysicalActivities_Volleyball(result);
            }

            if(getChildrenPhysicalActivitiesResponse.containsKey(ResponseKeys.ChartsController_physicalActivities_Other)){
                Double result = getChildrenPhysicalActivitiesResponse.get(ResponseKeys.ChartsController_physicalActivities_Other);
                dataset.setPhysicalActivities_Other(result);
            }


            ResponseEntity<?> dontHaveTimeToCookVegetables = dontHaveTimeToCookVegetables(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            System.out.println(dontHaveTimeToCookVegetables.getBody());
            Map<String, Double> dontHaveTimeToCookVegetablesResponse = (Map<String, Double>) dontHaveTimeToCookVegetables.getBody();

            if(dontHaveTimeToCookVegetablesResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = dontHaveTimeToCookVegetablesResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setParentsDontHaveTimeToCookVegetables(result);
            }

            ResponseEntity<?> childrenHomeBreakfast = getChildrenHomeBreakfast(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> childrenHomeBreakfastResponse =  (Map<String, Double>) (childrenHomeBreakfast.getBody());

            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenEatBreakfastAtHome(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Male_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Male_Percentage);
                dataset.setChildrenEatBreakfastAtHomeMale(result);
            }
            if(childrenHomeBreakfastResponse.containsKey(ResponseKeys.ChartsController_Female_Percentage)){
                Double result = childrenHomeBreakfastResponse.get(ResponseKeys.ChartsController_Female_Percentage);
                dataset.setChildrenEatBreakfastAtHomeFemale(result);
            }

            ResponseEntity<?> getChildrenBreakfastFrequency = getChildrenBreakfastFrequency(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> getChildrenBreakfastFrequencyResponse =  (Map<String, Double>) (getChildrenBreakfastFrequency.getBody());

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_EveryDay_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_EveryDay_Percentage);
                dataset.setBreakfastFrequency(result);
            }

            if(getChildrenBreakfastFrequencyResponse.containsKey(ResponseKeys.ChartsController_Never_Percentage)){
                Double result = getChildrenBreakfastFrequencyResponse.get(ResponseKeys.ChartsController_Never_Percentage);
                dataset.setBreakfastFrequencyNever(result);
            }

            ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals = getChildrenWhichEatSnacksBetweenMeals(null, domain, "2", String.valueOf(areaEntity.getIdarea()));

            Map<String, Double> getChildrenWhichEatSnacksBetweenMealsResponse =  (Map<String, Double>) (getChildrenWhichEatSnacksBetweenMeals.getBody());

            if(getChildrenWhichEatSnacksBetweenMealsResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatSnacksBetweenMealsResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatSnacksBetweenMeals(result);
            }


            ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool = getChildrenWhichEatFruitProvidedBySchool(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> getChildrenWhichEatFruitProvidedBySchoolResponse =  (Map<String, Double>) (getChildrenWhichEatFruitProvidedBySchool.getBody());

            if(getChildrenWhichEatFruitProvidedBySchoolResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = getChildrenWhichEatFruitProvidedBySchoolResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setChildrenWhichEatFruitProvidedBySchool(result);
            }


            ResponseEntity<?> organicFruitsOrVegetablesExpensive = organicFruitsOrVegetablesExpensive(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> organicFruitsOrVegetablesExpensiveResponse =  (Map<String, Double>) (organicFruitsOrVegetablesExpensive.getBody());


            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicFood_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicFood_Percentage);
                dataset.setParentsBelieveThatOrganicFoodIsExpensive(result);
            }

            if(organicFruitsOrVegetablesExpensiveResponse.containsKey(ResponseKeys.ChartsController_OrganicVegetables_Percentage)){
                Double result = organicFruitsOrVegetablesExpensiveResponse.get(ResponseKeys.ChartsController_OrganicVegetables_Percentage);
                dataset.setParentsBelieveThatOrganicVegetablesAreExpensive(result);
            }

            ResponseEntity<?> smoothiesAvailableHome = smoothiesAvailableHome(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> smoothiesAvailableHomeResponse =  (Map<String, Double>) (smoothiesAvailableHome.getBody());

            if(smoothiesAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = smoothiesAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setSmoothiesAreAvailableHome(result);
            }


            ResponseEntity<?> fruitsAvailableHome = fruitsAvailableHome(null, domain, "2", String.valueOf(areaEntity.getIdarea()));
            Map<String, Double> fruitsAvailableHomeResponse =  (Map<String, Double>) (fruitsAvailableHome.getBody());

            if(fruitsAvailableHomeResponse.containsKey(ResponseKeys.ChartsController_Total_Percentage)){
                Double result = fruitsAvailableHomeResponse.get(ResponseKeys.ChartsController_Total_Percentage);
                dataset.setFruitsAreAvailableHome(result);
            }

            datasets.add(dataset);

            MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
            File convFile = new File("publicFoodAreaStatistics.json");
            String json = new Gson().toJson(datasets);

            try {
                if(!convFile.exists())
                    convFile.createNewFile();
                else
                {
                    convFile.delete();
                    convFile.createNewFile();
                }

                FileUtils.writeStringToFile(
                        convFile, json, StandardCharsets.UTF_8, true);
                multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
                multipartMap.add("uuid", topPercentageByAreaUUID);
                multipartMap.add("collection", "publicFoodAreaStatistics_"+domain);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.postForEntity(
                        mongoUrl + update,
                        request, String.class);
                convFile.delete();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        return new ResponseEntity<List<Dataset>> (datasets, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts2/datasetsTopPercentageByArea", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsTopPercentageByArea(@RequestParam String domain) {

        List<StatisticsResults> datasets = new ArrayList<>();


        ResponseEntity<?> getTopPercentageChildrenWhichEatSnacksBetweenMeals = getTopPercentageChildrenWhichEatSnacksBetweenMeals(null, domain, "2");
        System.out.println(getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody());
        StatisticsResults getChildrenSnacksStatisticsResponse = (StatisticsResults) getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody();

        getChildrenSnacksStatisticsResponse.setType(StatisticsResults.Type.AREA.getValue());
        getChildrenSnacksStatisticsResponse.setDomain(domain);
        getChildrenSnacksStatisticsResponse.setQuestionType(StatisticsResults.QuestionType.SnacksBetweenMeals.getValue());
        datasets.add(getChildrenSnacksStatisticsResponse);

        ResponseEntity<?> getTopPercentageChildrenWhichEatFruitProvidedBySchool = getTopPercentageChildrenWhichEatFruitProvidedBySchool(null, domain, "2");
        System.out.println(getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody());
        StatisticsResults getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse = (StatisticsResults) getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody();

        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setDomain(domain);
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setQuestionType(StatisticsResults.QuestionType.EatFruitProvidedBySchool.getValue());
        datasets.add(getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse);

        ResponseEntity<?> getTopPercentagedontHaveTimeToCookVegetables = getTopPercentagedontHaveTimeToCookVegetables(null, domain, "2");
        System.out.println(getTopPercentagedontHaveTimeToCookVegetables.getBody());
        StatisticsResults getTopPercentagedontHaveTimeToCookVegetablesResponse = (StatisticsResults) getTopPercentagedontHaveTimeToCookVegetables.getBody();

        getTopPercentagedontHaveTimeToCookVegetablesResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setDomain(domain);
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setQuestionType(StatisticsResults.QuestionType.DontHaveTimeToCookVegetables.getValue());

        datasets.add(getTopPercentagedontHaveTimeToCookVegetablesResponse);

        ResponseEntity<?> getTopPercentagesmoothiesAvailableHome = getTopPercentagesmoothiesAvailableHome(null, domain, "2");
        System.out.println(getTopPercentagesmoothiesAvailableHome.getBody());
        StatisticsResults getTopPercentagesmoothiesAvailableHomeResponse = (StatisticsResults) getTopPercentagesmoothiesAvailableHome.getBody();

        getTopPercentagesmoothiesAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagesmoothiesAvailableHomeResponse.setDomain(domain);
        getTopPercentagesmoothiesAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.SmoothiesAvailableHome.getValue());

        datasets.add(getTopPercentagesmoothiesAvailableHomeResponse);

        ResponseEntity<?> getTopPercentagefruitsAvailableHome = getTopPercentagefruitsAvailableHome(null, domain, "2");
        System.out.println(getTopPercentagefruitsAvailableHome.getBody());
        StatisticsResults getTopPercentagefruitsAvailableHomeResponse = (StatisticsResults) getTopPercentagefruitsAvailableHome.getBody();

        getTopPercentagefruitsAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagefruitsAvailableHomeResponse.setDomain(domain);
        getTopPercentagefruitsAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.FruitsAvailableHome.getValue());

        datasets.add(getTopPercentagefruitsAvailableHomeResponse);



        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        File convFile = new File("topPercentages1.json");
        String json = new Gson().toJson(datasets);

        try {
            if(!convFile.exists())
                convFile.createNewFile();
            else
            {
                convFile.delete();
                convFile.createNewFile();
            }

            FileUtils.writeStringToFile(
                    convFile, json, StandardCharsets.UTF_8, true);
            multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
            multipartMap.add("uuid", topPercentageByAreaUUID);
            multipartMap.add("collection", "publicFoodTopPercentagesByArea_"+domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mongoUrl + update,
                    request, String.class);
            convFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<List<StatisticsResults>> (datasets, HttpStatus.OK);

    }


    @RequestMapping(value = "/charts2/datasetsTopPercentageBySchool", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsTopPercentageBySchool(@RequestParam String domain) {

        List<StatisticsResults> datasets = new ArrayList<>();


        ResponseEntity<?> getTopPercentageChildrenWhichEatSnacksBetweenMeals = getTopPercentageChildrenWhichEatSnacksBetweenMeals(null, domain, "0");
        System.out.println(getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody());
        StatisticsResults getChildrenSnacksStatisticsResponse = (StatisticsResults) getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody();

        getChildrenSnacksStatisticsResponse.setType(StatisticsResults.Type.AREA.getValue());
        getChildrenSnacksStatisticsResponse.setDomain(domain);
        getChildrenSnacksStatisticsResponse.setQuestionType(StatisticsResults.QuestionType.SnacksBetweenMeals.getValue());
        datasets.add(getChildrenSnacksStatisticsResponse);

        ResponseEntity<?> getTopPercentageChildrenWhichEatFruitProvidedBySchool = getTopPercentageChildrenWhichEatFruitProvidedBySchool(null, domain, "0");
        System.out.println(getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody());
        StatisticsResults getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse = (StatisticsResults) getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody();

        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setDomain(domain);
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setQuestionType(StatisticsResults.QuestionType.EatFruitProvidedBySchool.getValue());
        datasets.add(getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse);

        ResponseEntity<?> getTopPercentagedontHaveTimeToCookVegetables = getTopPercentagedontHaveTimeToCookVegetables(null, domain, "0");
        System.out.println(getTopPercentagedontHaveTimeToCookVegetables.getBody());
        StatisticsResults getTopPercentagedontHaveTimeToCookVegetablesResponse = (StatisticsResults) getTopPercentagedontHaveTimeToCookVegetables.getBody();

        getTopPercentagedontHaveTimeToCookVegetablesResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setDomain(domain);
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setQuestionType(StatisticsResults.QuestionType.DontHaveTimeToCookVegetables.getValue());

        datasets.add(getTopPercentagedontHaveTimeToCookVegetablesResponse);

        ResponseEntity<?> getTopPercentagesmoothiesAvailableHome = getTopPercentagesmoothiesAvailableHome(null, domain, "0");
        System.out.println(getTopPercentagesmoothiesAvailableHome.getBody());
        StatisticsResults getTopPercentagesmoothiesAvailableHomeResponse = (StatisticsResults) getTopPercentagesmoothiesAvailableHome.getBody();

        getTopPercentagesmoothiesAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagesmoothiesAvailableHomeResponse.setDomain(domain);
        getTopPercentagesmoothiesAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.SmoothiesAvailableHome.getValue());

        datasets.add(getTopPercentagesmoothiesAvailableHomeResponse);

        ResponseEntity<?> getTopPercentagefruitsAvailableHome = getTopPercentagefruitsAvailableHome(null, domain, "0");
        System.out.println(getTopPercentagefruitsAvailableHome.getBody());
        StatisticsResults getTopPercentagefruitsAvailableHomeResponse = (StatisticsResults) getTopPercentagefruitsAvailableHome.getBody();

        getTopPercentagefruitsAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagefruitsAvailableHomeResponse.setDomain(domain);
        getTopPercentagefruitsAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.FruitsAvailableHome.getValue());

        datasets.add(getTopPercentagefruitsAvailableHomeResponse);


        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        File convFile = new File("topPercentages2.json");
        String json = new Gson().toJson(datasets);

        try {
            if(!convFile.exists())
                convFile.createNewFile();
            else
            {
                convFile.delete();
                convFile.createNewFile();
            }

            FileUtils.writeStringToFile(
                    convFile, json, StandardCharsets.UTF_8, true);
            multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
            multipartMap.add("uuid", topPercentageBySchoolUUID);
            multipartMap.add("collection", "publicFoodTopPercentagesBySchool_" + domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mongoUrl + update,
                    request, String.class);
            convFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }




        return new ResponseEntity<List<StatisticsResults>> (datasets, HttpStatus.OK);

    }

    @RequestMapping(value = "/charts2/datasetsTopPercentageByPostCode", method = RequestMethod.GET)
    public ResponseEntity<?> getDatasetsTopPercentageByPostCode(@RequestParam String domain) {

        List<StatisticsResults> datasets = new ArrayList<>();


        ResponseEntity<?> getTopPercentageChildrenWhichEatSnacksBetweenMeals = getTopPercentageChildrenWhichEatSnacksBetweenMeals(null, domain, "3");
        System.out.println(getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody());
        StatisticsResults getChildrenSnacksStatisticsResponse = (StatisticsResults) getTopPercentageChildrenWhichEatSnacksBetweenMeals.getBody();

        getChildrenSnacksStatisticsResponse.setType(StatisticsResults.Type.AREA.getValue());
        getChildrenSnacksStatisticsResponse.setDomain(domain);
        getChildrenSnacksStatisticsResponse.setQuestionType(StatisticsResults.QuestionType.SnacksBetweenMeals.getValue());
        datasets.add(getChildrenSnacksStatisticsResponse);

        ResponseEntity<?> getTopPercentageChildrenWhichEatFruitProvidedBySchool = getTopPercentageChildrenWhichEatFruitProvidedBySchool(null, domain, "3");
        System.out.println(getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody());
        StatisticsResults getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse = (StatisticsResults) getTopPercentageChildrenWhichEatFruitProvidedBySchool.getBody();

        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setDomain(domain);
        getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse.setQuestionType(StatisticsResults.QuestionType.EatFruitProvidedBySchool.getValue());
        datasets.add(getTopPercentageChildrenWhichEatFruitProvidedBySchoolResponse);

        ResponseEntity<?> getTopPercentagedontHaveTimeToCookVegetables = getTopPercentagedontHaveTimeToCookVegetables(null, domain, "3");
        System.out.println(getTopPercentagedontHaveTimeToCookVegetables.getBody());
        StatisticsResults getTopPercentagedontHaveTimeToCookVegetablesResponse = (StatisticsResults) getTopPercentagedontHaveTimeToCookVegetables.getBody();

        getTopPercentagedontHaveTimeToCookVegetablesResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setDomain(domain);
        getTopPercentagedontHaveTimeToCookVegetablesResponse.setQuestionType(StatisticsResults.QuestionType.DontHaveTimeToCookVegetables.getValue());

        datasets.add(getTopPercentagedontHaveTimeToCookVegetablesResponse);

        ResponseEntity<?> getTopPercentagesmoothiesAvailableHome = getTopPercentagesmoothiesAvailableHome(null, domain, "3");
        System.out.println(getTopPercentagesmoothiesAvailableHome.getBody());
        StatisticsResults getTopPercentagesmoothiesAvailableHomeResponse = (StatisticsResults) getTopPercentagesmoothiesAvailableHome.getBody();

        getTopPercentagesmoothiesAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagesmoothiesAvailableHomeResponse.setDomain(domain);
        getTopPercentagesmoothiesAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.SmoothiesAvailableHome.getValue());

        datasets.add(getTopPercentagesmoothiesAvailableHomeResponse);

        ResponseEntity<?> getTopPercentagefruitsAvailableHome = getTopPercentagefruitsAvailableHome(null, domain, "3");
        System.out.println(getTopPercentagefruitsAvailableHome.getBody());
        StatisticsResults getTopPercentagefruitsAvailableHomeResponse = (StatisticsResults) getTopPercentagefruitsAvailableHome.getBody();

        getTopPercentagefruitsAvailableHomeResponse.setType(StatisticsResults.Type.AREA.getValue());
        getTopPercentagefruitsAvailableHomeResponse.setDomain(domain);
        getTopPercentagefruitsAvailableHomeResponse.setQuestionType(StatisticsResults.QuestionType.FruitsAvailableHome.getValue());

        datasets.add(getTopPercentagefruitsAvailableHomeResponse);


        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        File convFile = new File("topPercentages3.json");
        String json = new Gson().toJson(datasets);

        try {
            if(!convFile.exists())
                convFile.createNewFile();
            else
            {
                convFile.delete();
                convFile.createNewFile();
            }
            FileUtils.writeStringToFile(
                    convFile, json, StandardCharsets.UTF_8, true);
            multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
            multipartMap.add("uuid", topPercentageByPostCodeUUID);
            multipartMap.add("collection", "publicFoodTopPercentagesByPostCode_" + domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mongoUrl + update,
                    request, String.class);
            convFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }




        return new ResponseEntity<List<StatisticsResults>> (datasets, HttpStatus.OK);

    }


}
