package gr.uoa.di.madgik.publicfood.security.controller;

import com.google.gson.Gson;
import gr.uoa.di.madgik.publicfood.Utilities.StatisticsResults;
import gr.uoa.di.madgik.publicfood.facades.Dataset;
import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.SurveyEntity;
import gr.uoa.di.madgik.publicfood.limesurvey.limesurveyDAO.SurveyAnswers;
import gr.uoa.di.madgik.publicfood.limesurvey.utilities.SurveyStrings;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import gr.uoa.di.madgik.publicfood.security.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
public class ChartsControllerNew {

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

    @Value("${json.server.get.json.file}")
    private String get;

    @Value("${datasets.topPercentage.ByPostCode}")
    private String topPercentageByPostCodeUUID;

    @Value("${datasets.topPercentage.ByArea}")
    private String topPercentageByAreaUUID;

    @Value("${datasets.topPercentage.BySchool}")
    private String topPercentageBySchoolUUID;

    @Value("${datasets.topPercentage.ByCity}")
    private String topPercentageByCityUUID;

    private RestTemplate restTemplate;


    @Autowired
    public void setMetadataService(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(value = "/charts/children/breakfastAtHome", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenHomeBreakfast(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {


        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getChildrenEatBreakfastAtHome());
                results.put(ResponseKeys.ChartsController_Male_Percentage, dataset.getChildrenEatBreakfastAtHomeMale());
                results.put(ResponseKeys.ChartsController_Female_Percentage, dataset.getChildrenEatBreakfastAtHomeFemale());
            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }
                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getChildrenEatBreakfastAtHome());
                results.put(ResponseKeys.ChartsController_Male_Percentage, selectedDataset.getChildrenEatBreakfastAtHomeMale());
                results.put(ResponseKeys.ChartsController_Female_Percentage, selectedDataset.getChildrenEatBreakfastAtHomeFemale());
            }


            return new ResponseEntity<>(results, HttpStatus.OK);
        }


//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
//        }
//        return new ResponseEntity<Map<String, Double>>(results, HttpStatus.OK);
    }


    @RequestMapping(value = "/charts/children/breakfastFrequency", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenBreakfastFrequency(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_EveryDay_Percentage, dataset.getBreakfastFrequency());
                results.put(ResponseKeys.ChartsController_Never_Percentage, dataset.getBreakfastFrequencyNever());

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }

                results.put(ResponseKeys.ChartsController_EveryDay_Percentage, selectedDataset.getBreakfastFrequency());
                results.put(ResponseKeys.ChartsController_Never_Percentage, selectedDataset.getBreakfastFrequencyNever());

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }


    @RequestMapping(value = "/charts/children/breakfastStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenBreakfasStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }


                results.put(SurveyStrings.breakfastPopularIngredients_milk_yogurt, dataset.getBreakfastPopularIngredients_milk_yogurt());
                results.put(SurveyStrings.breakfastPopularIngredients_biscuits_cake, dataset.getBreakfastPopularIngredients_biscuits_cake());
                results.put(SurveyStrings.breakfastPopularIngredients_bread, dataset.getBreakfastPopularIngredients_bread());
                results.put(SurveyStrings.breakfastPopularIngredients_snacks, dataset.getBreakfastPopularIngredients_snacks());
                results.put(SurveyStrings.breakfastPopularIngredients_cereal, dataset.getBreakfastPopularIngredients_cereal());
                results.put(SurveyStrings.breakfastPopularIngredients_dried_fruits, dataset.getBreakfastPopularIngredients_dried_fruits());
                results.put(SurveyStrings.breakfastPopularIngredients_tea, dataset.getBreakfastPopularIngredients_tea());
                results.put(SurveyStrings.breakfastPopularIngredients_fruit_juice, dataset.getBreakfastPopularIngredients_fruit_juice());
                results.put(SurveyStrings.breakfastPopularIngredients_coffee, dataset.getBreakfastPopularIngredients_coffee());
                results.put(SurveyStrings.breakfastPopularIngredients_eggs, dataset.getBreakfastPopularIngredients_eggs());

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }

                results.put(SurveyStrings.breakfastPopularIngredients_milk_yogurt, selectedDataset.getBreakfastPopularIngredients_milk_yogurt());
                results.put(SurveyStrings.breakfastPopularIngredients_biscuits_cake, selectedDataset.getBreakfastPopularIngredients_biscuits_cake());
                results.put(SurveyStrings.breakfastPopularIngredients_bread, selectedDataset.getBreakfastPopularIngredients_bread());
                results.put(SurveyStrings.breakfastPopularIngredients_snacks, selectedDataset.getBreakfastPopularIngredients_snacks());
                results.put(SurveyStrings.breakfastPopularIngredients_cereal, selectedDataset.getBreakfastPopularIngredients_cereal());
                results.put(SurveyStrings.breakfastPopularIngredients_dried_fruits, selectedDataset.getBreakfastPopularIngredients_dried_fruits());
                results.put(SurveyStrings.breakfastPopularIngredients_tea, selectedDataset.getBreakfastPopularIngredients_tea());
                results.put(SurveyStrings.breakfastPopularIngredients_fruit_juice, selectedDataset.getBreakfastPopularIngredients_fruit_juice());
                results.put(SurveyStrings.breakfastPopularIngredients_coffee, selectedDataset.getBreakfastPopularIngredients_coffee());
                results.put(SurveyStrings.breakfastPopularIngredients_eggs, selectedDataset.getBreakfastPopularIngredients_eggs());

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/chiildren/typesOfPhysicalActivities", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenPhysicalActivities(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }


                results.put(ResponseKeys.ChartsController_physicalActivities_Athletics, dataset.getPhysicalActivities_Athletics());
                results.put(ResponseKeys.ChartsController_physicalActivities_Basketball, dataset.getPhysicalActivities_Basketball());
                results.put(ResponseKeys.ChartsController_physicalActivities_Cycling, dataset.getPhysicalActivities_Cycling());
                results.put(ResponseKeys.ChartsController_physicalActivities_Dancing, dataset.getPhysicalActivities_Dancing());
                results.put(ResponseKeys.ChartsController_physicalActivities_Gymnastics, dataset.getPhysicalActivities_Gymnastics());
                results.put(ResponseKeys.ChartsController_physicalActivities_Soccer, dataset.getPhysicalActivities_Soccer());
                results.put(ResponseKeys.ChartsController_physicalActivities_Swimming, dataset.getPhysicalActivities_Swimming());
                results.put(ResponseKeys.ChartsController_physicalActivities_Tennis, dataset.getPhysicalActivities_Tennis());
                results.put(ResponseKeys.ChartsController_physicalActivities_Volleyball, dataset.getPhysicalActivities_Volleyball());
                results.put(ResponseKeys.ChartsController_physicalActivities_Other, dataset.getPhysicalActivities_Other());
            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }

                results.put(ResponseKeys.ChartsController_physicalActivities_Athletics, selectedDataset.getPhysicalActivities_Athletics());
                results.put(ResponseKeys.ChartsController_physicalActivities_Basketball, selectedDataset.getPhysicalActivities_Basketball());
                results.put(ResponseKeys.ChartsController_physicalActivities_Cycling, selectedDataset.getPhysicalActivities_Cycling());
                results.put(ResponseKeys.ChartsController_physicalActivities_Dancing, selectedDataset.getPhysicalActivities_Dancing());
                results.put(ResponseKeys.ChartsController_physicalActivities_Gymnastics, selectedDataset.getPhysicalActivities_Gymnastics());
                results.put(ResponseKeys.ChartsController_physicalActivities_Soccer, selectedDataset.getPhysicalActivities_Soccer());
                results.put(ResponseKeys.ChartsController_physicalActivities_Swimming, selectedDataset.getPhysicalActivities_Swimming());
                results.put(ResponseKeys.ChartsController_physicalActivities_Tennis, selectedDataset.getPhysicalActivities_Tennis());
                results.put(ResponseKeys.ChartsController_physicalActivities_Volleyball, selectedDataset.getPhysicalActivities_Volleyball());
                results.put(ResponseKeys.ChartsController_physicalActivities_Other, selectedDataset.getPhysicalActivities_Other());
            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/exerciseStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenExerciseStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }


                results.put(ResponseKeys.ChartsController_exerciseHours_0_1_hours, dataset.getExerciseHours_0_1_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_1_3_hours, dataset.getExerciseHours_1_3_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_3_7_hours, dataset.getExerciseHours_3_7_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours, dataset.getExerciseHours_moreThan_7_hours());
            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_exerciseHours_0_1_hours, selectedDataset.getExerciseHours_0_1_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_1_3_hours, selectedDataset.getExerciseHours_1_3_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_3_7_hours, selectedDataset.getExerciseHours_3_7_hours());
                results.put(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours, selectedDataset.getExerciseHours_moreThan_7_hours());
            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/snacksStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenSnacksStatistics(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }

                results.put(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables, dataset.getFavorite_snacks_Fruit_Or_Vegetables());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar, dataset.getFavorite_snacks_Savoury_Snack_Bar());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar, dataset.getFavorite_snacks_Sweet_Snack_Bar());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Nuts, dataset.getFavorite_snacks_Nuts());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Chocolate, dataset.getFavorite_snacks_Chocolate());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Candy, dataset.getFavorite_snacks_Candy());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits, dataset.getFavorite_snacks_Cakes_And_Biscuits());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat, dataset.getFavorite_snacks_Bread_And_Cured_Meat());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Other, dataset.getFavorite_snacks_Other());

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables, selectedDataset.getFavorite_snacks_Fruit_Or_Vegetables());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar, selectedDataset.getFavorite_snacks_Savoury_Snack_Bar());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar, selectedDataset.getFavorite_snacks_Sweet_Snack_Bar());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Nuts, selectedDataset.getFavorite_snacks_Nuts());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Chocolate, selectedDataset.getFavorite_snacks_Chocolate());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Candy, selectedDataset.getFavorite_snacks_Candy());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits, selectedDataset.getFavorite_snacks_Cakes_And_Biscuits());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat, selectedDataset.getFavorite_snacks_Bread_And_Cured_Meat());
                results.put(ResponseKeys.ChartsController_favorite_snacks_Other, selectedDataset.getFavorite_snacks_Other());
            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/snacksBetweenMeals", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenWhichEatSnacksBetweenMeals(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getChildrenWhichEatSnacksBetweenMeals());

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getChildrenWhichEatSnacksBetweenMeals());

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/snacksBetweenMealsTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentageChildrenWhichEatSnacksBetweenMeals(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {
        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodTopPercentagesBySchool_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodTopPercentagesByPostCode_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodTopPercentagesByArea_" + domain;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {

            JSONArray jsonArray = new JSONArray(result.getBody());

            StatisticsResults selectedDataset = new StatisticsResults();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                StatisticsResults dataset = new StatisticsResults().Deserialize(obj);
                if (dataset.getQuestionType() == StatisticsResults.QuestionType.SnacksBetweenMeals.getValue()) {
                    selectedDataset = dataset;
                    break;
                }
            }


            return new ResponseEntity<>(selectedDataset, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/EatFruitProvidedBySchoolTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentageChildrenWhichEatFruitProvidedBySchool(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodTopPercentagesBySchool_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodTopPercentagesByPostCode_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodTopPercentagesByArea_" + domain;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {

            JSONArray jsonArray = new JSONArray(result.getBody());

            StatisticsResults selectedDataset = new StatisticsResults();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                StatisticsResults dataset = new StatisticsResults().Deserialize(obj);
                if (dataset.getQuestionType() == StatisticsResults.QuestionType.EatFruitProvidedBySchool.getValue()) {
                    selectedDataset = dataset;
                    break;
                }
            }


            return new ResponseEntity<>(selectedDataset, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/children/EatFruitProvidedBySchool", method = RequestMethod.GET)
    public ResponseEntity<?> getChildrenWhichEatFruitProvidedBySchool(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getChildrenWhichEatFruitProvidedBySchool());

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getChildrenWhichEatSnacksBetweenMeals());

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/believeThatOrganicFoodIsExpensive", method = RequestMethod.GET)
    public ResponseEntity<?> organicFruitsOrVegetablesExpensive(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_OrganicFood_Percentage, dataset.getParentsBelieveThatOrganicFoodIsExpensive() );
                results.put(ResponseKeys.ChartsController_OrganicVegetables_Percentage, dataset.getParentsBelieveThatOrganicVegetablesAreExpensive() );
            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_OrganicFood_Percentage, selectedDataset.getParentsBelieveThatOrganicFoodIsExpensive() );
                results.put(ResponseKeys.ChartsController_OrganicVegetables_Percentage, selectedDataset.getParentsBelieveThatOrganicVegetablesAreExpensive() );

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/fruitsAreAvailableHome", method = RequestMethod.GET)
    public ResponseEntity<?> fruitsAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getFruitsAreAvailableHome() );
            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getFruitsAreAvailableHome() );

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/fruitsAreAvailableHomeTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagefruitsAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodTopPercentagesBySchool_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodTopPercentagesByPostCode_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodTopPercentagesByArea_" + domain;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {

            JSONArray jsonArray = new JSONArray(result.getBody());

            StatisticsResults selectedDataset = new StatisticsResults();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                StatisticsResults dataset = new StatisticsResults().Deserialize(obj);
                if (dataset.getQuestionType() == StatisticsResults.QuestionType.FruitsAvailableHome.getValue()) {
                    selectedDataset = dataset;
                    break;
                }
            }


            return new ResponseEntity<>(selectedDataset, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/smoothiesAreAvailableHome", method = RequestMethod.GET)
    public ResponseEntity<?> smoothiesAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getSmoothiesAreAvailableHome() );

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getSmoothiesAreAvailableHome() );

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);

    }

    @RequestMapping(value = "/charts/parents/smoothies/smoothiesAreAvailableHomeTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagesmoothiesAvailableHome(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodTopPercentagesBySchool_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodTopPercentagesByPostCode_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodTopPercentagesByArea_" + domain;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {

            JSONArray jsonArray = new JSONArray(result.getBody());

            StatisticsResults selectedDataset = new StatisticsResults();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                StatisticsResults dataset = new StatisticsResults().Deserialize(obj);
                if (dataset.getQuestionType() == StatisticsResults.QuestionType.SmoothiesAvailableHome.getValue()) {
                    selectedDataset = dataset;
                    break;
                }
            }


            return new ResponseEntity<>(selectedDataset, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/doNotHaveTimeToCookVegetables", method = RequestMethod.GET)
    public ResponseEntity<?> dontHaveTimeToCookVegetables(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria", defaultValue = "null") final String criteria, @RequestParam(value = "criteriaValue", defaultValue = "null") final String criteriaValue) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodSchoolsStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.CITY.getValue()) {
            id = topPercentageByCityUUID;
            group = "publicFoodCityStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodPostCodeStatistics_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodAreaStatistics_" + domain;
        }

        Map<String, Double> results = new HashMap<String, Double>();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {


            if (c == SurveyEntity.Criteria.CITY.getValue()) {

                JSONArray jsonArray = new JSONArray(result.getBody());

                //       for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(0);

                Dataset dataset = new Dataset().Deserialize(obj);


                //     }
                results.put(ResponseKeys.ChartsController_Total_Percentage, dataset.getParentsDontHaveTimeToCookVegetables() );

            } else {
                JSONArray jsonArray = new JSONArray(result.getBody());

                Dataset selectedDataset = new Dataset();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    Dataset dataset = new Dataset().Deserialize(obj);
                    if (dataset.getId() == Integer.parseInt(criteriaValue)) {
                        selectedDataset = dataset;
                        break;
                    }
                }


                results.put(ResponseKeys.ChartsController_Total_Percentage, selectedDataset.getParentsDontHaveTimeToCookVegetables() );

            }

            return new ResponseEntity<>(results, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/charts/parents/doNotHaveTimeToCookVegetablesTopPercentage", method = RequestMethod.GET)
    public ResponseEntity<?> getTopPercentagedontHaveTimeToCookVegetables(HttpServletRequest request, @RequestParam String domain, @RequestParam(value = "criteria") final String criteria) {

        String id = null, group = null;
        int c;
        if (criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if (c == SurveyEntity.Criteria.SCHOOL.getValue()) {
            id = topPercentageBySchoolUUID;
            group = "publicFoodTopPercentagesBySchool_" + domain;
        } else if (c == SurveyEntity.Criteria.POSTCODE.getValue()) {
            id = topPercentageByPostCodeUUID;
            group = "publicFoodTopPercentagesByPostCode_" + domain;
        } else if (c == SurveyEntity.Criteria.AREA.getValue()) {
            id = topPercentageByAreaUUID;
            group = "publicFoodTopPercentagesByArea_" + domain;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mongoUrl + get + id).queryParam("collection", group);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println(builder.build().encode().toUri());
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        if (result.getStatusCode() == HttpStatus.OK) {

            JSONArray jsonArray = new JSONArray(result.getBody());

            StatisticsResults selectedDataset = new StatisticsResults();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                StatisticsResults dataset = new StatisticsResults().Deserialize(obj);
                if (dataset.getQuestionType() == StatisticsResults.QuestionType.DontHaveTimeToCookVegetables.getValue()) {
                    selectedDataset = dataset;
                    break;
                }
            }


            return new ResponseEntity<>(selectedDataset, HttpStatus.OK);
        }
//        if(results == null){
        Map<String, String> error = new HashMap<String, String>();
        error.put("error", "No available answers");
        return new ResponseEntity<Map<String, String>>(error, HttpStatus.OK);
    }

    }
