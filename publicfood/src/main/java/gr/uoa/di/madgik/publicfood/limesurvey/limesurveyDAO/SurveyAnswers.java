package gr.uoa.di.madgik.publicfood.limesurvey.limesurveyDAO;

import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.SurveyEntity;
import gr.uoa.di.madgik.publicfood.limesurvey.utilities.SurveyStrings;
import gr.uoa.di.madgik.publicfood.limesurvey.utilities.Utils;
import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import gr.uoa.di.madgik.publicfood.security.repository.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SurveyAnswers {


//    public static SurveyEntity lastValidAnswer(List<SurveyEntity> answers)
//    {
//        List<SurveyEntity> completedAnswers = new ArrayList<SurveyEntity>();
//        for(SurveyEntity s : answers)
//        {
//            if(s.getSubmitdate() != null)
//            {
//                completedAnswers.add(s);
//            }
//        }
//
//        Collections.sort(completedAnswers, new Comparator<SurveyEntity>() {
//            public int compare(SurveyEntity o1, SurveyEntity o2) {
//                return o1.getSubmitdate().compareTo(o2.getSubmitdate());
//            }
//        });
//
//        if(!completedAnswers.isEmpty())
//            return completedAnswers.get(completedAnswers.size() - 1);
//
//        return null;
//
//    }


    public static List<SurveyEntity> getSurveyAnswers(UserRepository userRepository, UserHasChildrenRepository userHasChildrenRepository, ChildRepository childRepository, SchoolRepository schoolRepository, CookingCenterRepository cookingCenterRepository, String criteria, String domain, String criteriaValue)
    {
        int c = -1;
        List <UserEntity> userEntities = new ArrayList<UserEntity>();
        List <SurveyEntity> surveyEntities = new ArrayList<SurveyEntity>();

        if(criteria != "null")
            c = Integer.parseInt(criteria);
        else
            return null;

        if( SurveyEntity.Criteria.CITY.getValue() == c ){
            userEntities = userRepository.findByDomainName(domain);
        }
        else if(SurveyEntity.Criteria.SCHOOL.getValue() == c){
            long startTime = System.currentTimeMillis();

            int schoolId = Integer.parseInt(criteriaValue);
            List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, schoolId);
            for(ChildrenEntity childrenEntity : childrens){
                UserHasChildrenEntity byChildrenIdchildrenAndDomainName = userHasChildrenRepository.findByChildrenIdchildrenAndDomainName(childrenEntity.getIdchildren(), domain);
                if(byChildrenIdchildrenAndDomainName != null) {
                    UserEntity byIdDomainName = userRepository.findByIdAndDomainName(byChildrenIdchildrenAndDomainName.getUserId(), domain);
                    if (childrenEntity.getGender() == ChildrenEntity.Gender.Male.getValue()) {
                        byIdDomainName.setMale(true);
                    }
                    if(byIdDomainName != null)
                        userEntities.add(byIdDomainName);
                }
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            int seconds = (int) (elapsedTime / 1000) % 60 ;
           // System.out.println("Database: " + elapsedTime);


        }
        else if(SurveyEntity.Criteria.POSTCODE.getValue() == c){
            int postCode = Integer.parseInt(criteriaValue);
            List<SchoolEntity> schools = schoolRepository.findByDomainNameAndPostCodeIdPostCode(domain, postCode);
            for (SchoolEntity schoolEntity : schools) {
                List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, schoolEntity.getId());
                for (ChildrenEntity childrenEntity : childrens) {
                    UserHasChildrenEntity byChildrenIdchildrenAndDomainName = userHasChildrenRepository.findByChildrenIdchildrenAndDomainName(childrenEntity.getIdchildren(), domain);
                    if (byChildrenIdchildrenAndDomainName != null) {
                        UserEntity byIdDomainName = userRepository.findByIdAndDomainName(byChildrenIdchildrenAndDomainName.getUserId(), domain);
                        if (childrenEntity.getGender() == ChildrenEntity.Gender.Male.getValue()) {
                            byIdDomainName.setMale(true);
                        }
                        if (byIdDomainName != null)
                            userEntities.add(byIdDomainName);
                    }
                }
            }
        }
        else if(SurveyEntity.Criteria.AREA.getValue() == c){
            int area = Integer.parseInt(criteriaValue);
            List<CookingCenterEntity> cookingCenterEntities = cookingCenterRepository.findByDomainNameAndArea(domain, area);
            for(CookingCenterEntity cookingCenterEntity : cookingCenterEntities) {
                List<SchoolEntity> schools = schoolRepository.findByDomainNameAndCookingCenterId(domain, cookingCenterEntity.getId());
                for (SchoolEntity schoolEntity : schools) {
                    List<ChildrenEntity> childrens = childRepository.findByDomainNameAndSchoolId(domain, schoolEntity.getId());
                    for (ChildrenEntity childrenEntity : childrens) {
                        UserHasChildrenEntity byChildrenIdchildrenAndDomainName = userHasChildrenRepository.findByChildrenIdchildrenAndDomainName(childrenEntity.getIdchildren(), domain);
                        if (byChildrenIdchildrenAndDomainName != null) {
                            UserEntity byIdDomainName = userRepository.findByIdAndDomainName(byChildrenIdchildrenAndDomainName.getUserId(), domain);
                            if (childrenEntity.getGender() == ChildrenEntity.Gender.Male.getValue()) {
                                byIdDomainName.setMale(true);
                            }
                            if (byIdDomainName != null)
                                userEntities.add(byIdDomainName);
                        }
                    }
                }
            }
        }

        long startTime = System.currentTimeMillis();

        for(UserEntity user : userEntities) {
            //List<SurveyEntity> answers = surveyRepository.findByToken(user.getLimeSurveyRegistrationToken());
           // SurveyEntity surveyEntity = SurveyAnswers.lastValidAnswer(answers);
            if(user.getStatus() == UserEntity.Status.FULLY_REGISTERED.getValue()) {
                SurveyEntity surveyEntity = null;
                try {
                    surveyEntity = Utils.getAnswerByToken(user.getLimeSurveyRegistrationToken());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (surveyEntity != null) {
                    surveyEntity.setMale(user.isMale());
                    surveyEntities.add(surveyEntity);
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        int seconds = (int) (elapsedTime / 1000) % 60 ;
      //  System.out.println("Limesurvey api: " + elapsedTime);

        return surveyEntities;

    }


    public static Map<String, Double> breakfastAtHome(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
        double malePercentage = 0.0;
        double female = 0.0;

        if(answers.isEmpty())
            return null;
        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question6 = s.getQuestion6();
            if(question6 != null && !question6.isEmpty()) {
                if (question6.equals(SurveyStrings.breakfast_at_home)){
                    totalCount++;
                    if(s.isMale()){
                        maleCount++;
                    }
                    else{
                        femaleCount++;
                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double) totalCount / (double) answers.size();
            malePercentage = (double)maleCount / (double)totalCount;
            female = (double)femaleCount / (double)totalCount;

            totalPercentage = round(totalPercentage * 100, 0);
            malePercentage = round(malePercentage * 100 , 0);
            female = round(female * 100, 0);

        }

        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
        results.put(ResponseKeys.ChartsController_Male_Percentage, malePercentage );
        results.put(ResponseKeys.ChartsController_Female_Percentage, female );

        return results;
    }

    public static Map<String, Double> breakfastFrequency(List<SurveyEntity> answers){

        double everyDayPercentage = 0.0;
        double neverPercentage = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int everyDayCount = 0, neverCount=0;
        for(SurveyEntity s : answers){
            String question3 = s.getQuestion3();
            if(question3 != null && !question3.isEmpty()) {
                if (question3.equals(SurveyStrings.breakfast_frequency_with_children_none)){
                    neverCount++;
                }
                else if (question3.equals(SurveyStrings.breakfast_frequency_with_children_every_day)){
                    everyDayCount++;
                }
            }
        }

        everyDayPercentage = (double)everyDayCount / (double)answers.size();
        neverPercentage = (double)neverCount / (double) answers.size();
        if(!answers.isEmpty()){
            everyDayPercentage = round(everyDayPercentage * 100, 0);
            neverPercentage = round(neverPercentage * 100, 0);

        }
        results.put(ResponseKeys.ChartsController_EveryDay_Percentage, everyDayPercentage );
        results.put(ResponseKeys.ChartsController_Never_Percentage, neverPercentage );

        return results;
    }

    public static Map<String, Double> breakfastPopularIngretients(List<SurveyEntity> answers){

        int[] res = new int[10];

        double[] percentages = new double[10];

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        for(SurveyEntity s : answers){
            if (s.getQuestion32().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[0]++;
            }
            if (s.getQuestion38().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[1]++;
            }if (s.getQuestion40().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[2]++;
            }if (s.getQuestion43().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[3]++;
            }if (s.getQuestion34().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[4]++;
            }if (s.getQuestion35().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[5]++;
            }if (s.getQuestion37().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[6]++;
            }if (s.getQuestion39().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[7]++;
            }if (s.getQuestion41().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[8]++;
            }if (s.getQuestion33().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[9]++;
            }
        }

        for(int i=0; i< res.length ;i++){
            percentages[i] = (double)res[i] / (double)answers.size() * 100;
            percentages[i] = round(percentages[i], 0);


        }
        results.put(SurveyStrings.breakfastPopularIngredients_milk_yogurt, percentages[0]);
        results.put(SurveyStrings.breakfastPopularIngredients_biscuits_cake, percentages[1]);
        results.put(SurveyStrings.breakfastPopularIngredients_bread, percentages[2]);
        results.put(SurveyStrings.breakfastPopularIngredients_snacks, percentages[3]);
        results.put(SurveyStrings.breakfastPopularIngredients_cereal, percentages[4]);
        results.put(SurveyStrings.breakfastPopularIngredients_dried_fruits, percentages[5]);
        results.put(SurveyStrings.breakfastPopularIngredients_tea, percentages[6]);
        results.put(SurveyStrings.breakfastPopularIngredients_fruit_juice, percentages[7]);
        results.put(SurveyStrings.breakfastPopularIngredients_coffee, percentages[8]);
        results.put(SurveyStrings.breakfastPopularIngredients_eggs, percentages[9]);


        return results;
    }


    public static Map<String, Double> physicalActivities(List<SurveyEntity> answers){

        int[] res = new int[10];
        double[] percentages = new double[10];
        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        for(SurveyEntity s : answers){
            String question74 = s.getQuestion74();
            if(!question74.isEmpty()){
                switch (question74){
                    case SurveyStrings.activities_Athletics:
                        res[0]++;
                        break;
                    case SurveyStrings.activities_Basketball:
                        res[1]++;
                        break;
                    case SurveyStrings.activities_Cycling:
                        res[2]++;
                        break;
                    case SurveyStrings.activities_Dancing:
                        res[3]++;
                        break;
                    case SurveyStrings.activities_Gymnastics:
                        res[4]++;
                        break;
                    case SurveyStrings.activities_Soccer:
                        res[5]++;
                        break;
                    case SurveyStrings.activities_Swimming:
                        res[6]++;
                        break;
                    case SurveyStrings.activities_Tennis:
                        res[7]++;
                        break;
                    case SurveyStrings.activities_Volleyball:
                        res[8]++;
                        break;
                    case SurveyStrings.activities_Other:
                        res[9]++;
                        break;
                }
            }
        }
        for(int i=0; i< res.length ;i++){
            percentages[i] = (double)res[i] / (double)answers.size() * 100;
            percentages[i] = round(percentages[i], 0);

        }

        results.put(ResponseKeys.ChartsController_physicalActivities_Athletics, percentages[0]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Basketball, percentages[1]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Cycling, percentages[2]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Dancing, percentages[3]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Gymnastics, percentages[4]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Soccer, percentages[5]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Swimming, percentages[6]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Tennis, percentages[7]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Volleyball, percentages[8]);
        results.put(ResponseKeys.ChartsController_physicalActivities_Other, percentages[9]);


        return results;
    }

    public static Map<String, Double> excerciseStatistics(List<SurveyEntity> answers){

        int[] res = new int[4];
        double[] percentages = new double[4];
        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        for(SurveyEntity s : answers) {
            String question73 = s.getQuestion73();
            if (!question73.isEmpty()) {
                switch (question73) {
                    case SurveyStrings.exerciseHours_0_1_hours:
                        res[0]++;
                        break;
                    case SurveyStrings.exerciseHours_1_3_hours:
                        res[1]++;
                        break;
                    case SurveyStrings.exerciseHours_3_7_hours:
                        res[2]++;
                        break;
                    case SurveyStrings.exerciseHours_moreThan_7_hours:
                        res[3]++;
                        break;
                }
            }
        }
        for(int i=0; i< res.length ;i++){
            percentages[i] = (double)res[i] /(double) answers.size() * 100;
            percentages[i] = round(percentages[i], 0);

        }
        results.put(ResponseKeys.ChartsController_exerciseHours_0_1_hours, percentages[0]);
        results.put(ResponseKeys.ChartsController_exerciseHours_1_3_hours, percentages[1]);
        results.put(ResponseKeys.ChartsController_exerciseHours_3_7_hours, percentages[2]);
        results.put(ResponseKeys.ChartsController_exerciseHours_moreThan_7_hours, percentages[3]);

        return results;
    }

    public static Map<String, Double> snacksStatistics(List<SurveyEntity> answers){

        int[] res = new int[9];

        double[] percentages = new double[9];

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        for(SurveyEntity s : answers){
            if (s.getQuestion47().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[0]++;
            }
            if (s.getQuestion48().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[1]++;
            }if (s.getQuestion49().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[2]++;
            }if (s.getQuestion50().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[3]++;
            }if (s.getQuestion51().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[4]++;
            }if (s.getQuestion52().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[5]++;
            }if (s.getQuestion53().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[6]++;
            }if (s.getQuestion54().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[7]++;
            }if (s.getQuestion55().equals(SurveyStrings.multiple_choice_yes_answer)){
                res[8]++;
            }
        }

        for(int i=0; i< res.length ;i++){
            percentages[i] = (double)res[i] / (double) answers.size() * 100;
            percentages[i] = round(percentages[i], 0);

        }

        results.put(ResponseKeys.ChartsController_favorite_snacks_Fruit_Or_Vegetables, percentages[0]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Savoury_Snack_Bar, percentages[1]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Sweet_Snack_Bar, percentages[2]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Nuts, percentages[3]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Chocolate, percentages[4]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Candy, percentages[5]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Cakes_And_Biscuits, percentages[6]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Bread_And_Cured_Meat, percentages[7]);
        results.put(ResponseKeys.ChartsController_favorite_snacks_Other, percentages[8]);

        return results;
    }


    public static Map<String, Double> snackBetweenMeals(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
        double malePercentage = 0.0;
        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question46 = s.getQuestion46();
            if(question46 != null && !question46.isEmpty()) {
                if (question46.equals(SurveyStrings.snack_between_meals_yes)){
                    totalCount++;
                    if(s.isMale()){
                        maleCount++;
                    }
                    else{
                        femaleCount++;
                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double)totalCount / (double)answers.size();
            malePercentage = (double)maleCount / (double)totalCount;
            female = (double)femaleCount / (double)totalCount;
            totalPercentage = round(totalPercentage* 100, 0);
            malePercentage = round(malePercentage* 100, 0);
            female = round(female* 100, 0);

        }

        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
        results.put(ResponseKeys.ChartsController_Male_Percentage, malePercentage );
        results.put(ResponseKeys.ChartsController_Female_Percentage, female );

        return results;
    }

    public static Map<String, Double>   fruitProvidedBySchool(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
        double malePercentage = 0.0;
        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question56 = s.getQuestion56();
            if(question56 != null && !question56.isEmpty()) {
                if (question56.equals(SurveyStrings.child_eats_fruit_provided_by_school)){
                    totalCount++;
                    if(s.isMale()){
                        maleCount++;
                    }
                    else{
                        femaleCount++;
                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double)totalCount / (double)answers.size();
            malePercentage = (double)maleCount / (double)totalCount;
            female = (double)femaleCount / (double)totalCount;
            totalPercentage = round(totalPercentage * 100, 0);
            malePercentage = round(malePercentage * 100, 0);
            female = round(female * 100, 0);

        }

        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
        results.put(ResponseKeys.ChartsController_Male_Percentage, malePercentage );
        results.put(ResponseKeys.ChartsController_Female_Percentage, female );

        return results;
    }


    public static Map<String, Double> expensiveOrganicFruitsAndVegetables(List<SurveyEntity> answers){

        double organicFruitTotalPercentage = 0.0, organicVegetablesTotalPercentage = 0.0;
//        double malePercentage = 0.0;
//        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int organicFruitTotalCount = 0, organicVegetablesTotalCount=0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question63 = s.getQuestion63();
            if(question63 != null && !question63.isEmpty()) {
                if (question63.equals(SurveyStrings.statement_agree) || question63.equals(SurveyStrings.statement_strongly_agree)){
                    organicFruitTotalCount++;
//                    if(s.isMale()){
//                        maleCount++;
//                    }
//                    else{
//                        femaleCount++;
//                    }
                }
            }
            String question62 = s.getQuestion62();

            if(question62 != null && !question62.isEmpty()) {
                if (question62.equals(SurveyStrings.statement_agree) || question62.equals(SurveyStrings.statement_strongly_agree)){
                    organicVegetablesTotalCount++;
//                    if(s.isMale()){
//                        maleCount++;
//                    }
//                    else{
//                        femaleCount++;
//                    }
                }
            }
        }

        if(organicFruitTotalCount > 0) {
            organicFruitTotalPercentage = (double)organicFruitTotalCount / (double)answers.size();
            organicFruitTotalPercentage = round(organicFruitTotalPercentage * 100, 0);


            //   malePercentage = maleCount / totalCount;
         //   female = femaleCount / totalCount;
        }


        if(organicVegetablesTotalCount > 0) {
            organicVegetablesTotalPercentage = (double)organicVegetablesTotalCount / (double)answers.size();
            organicVegetablesTotalPercentage = round(organicVegetablesTotalPercentage * 100, 0);


            //   malePercentage = maleCount / totalCount;
            //   female = femaleCount / totalCount;
        }

        results.put(ResponseKeys.ChartsController_OrganicFood_Percentage, organicFruitTotalPercentage );
        results.put(ResponseKeys.ChartsController_OrganicVegetables_Percentage, organicVegetablesTotalPercentage );

        // results.put("male", malePercentage * 100);
        // results.put("female", female * 100);

        return results;
    }

    public static Map<String, Double> fruitAvailableAtHome(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
//        double malePercentage = 0.0;
//        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question58 = s.getQuestion58();
            if(question58 != null && !question58.isEmpty()) {
                if (question58.equals(SurveyStrings.statement_strongly_agree) || question58.equals(SurveyStrings.statement_agree)){
                    totalCount++;
//                    if(s.isMale()){
//                        maleCount++;
//                    }
//                    else{
//                        femaleCount++;
//                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double)totalCount / (double)answers.size();
            totalPercentage = round(totalPercentage * 100, 0);

//            malePercentage = maleCount / totalCount;
//            female = femaleCount / totalCount;
        }

        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
//        results.put("male", malePercentage * 100);
//        results.put("female", female * 100);

        return results;
    }

    public static Map<String, Double> juicesAndFruitAtHome(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
//        double malePercentage = 0.0;
//        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question60 = s.getQuestion60();
            if(question60 != null && !question60.isEmpty()) {
                if (question60.equals(SurveyStrings.statement_agree) || question60.equals(SurveyStrings.statement_strongly_agree)){
                    totalCount++;
//                    if(s.isMale()){
//                        maleCount++;
//                    }
//                    else{
//                        femaleCount++;
//                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double)totalCount /(double) answers.size();
            totalPercentage = round(totalPercentage * 100, 0);

//            malePercentage = maleCount / totalCount;
//            female = femaleCount / totalCount;
        }

        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
//        results.put("male", malePercentage * 100);
//        results.put("female", female * 100);

        return results;
    }

    public static Map<String, Double> dontCookVegetables(List<SurveyEntity> answers){

        double totalPercentage = 0.0;
//        double malePercentage = 0.0;
//        double female = 0.0;

        if(answers.isEmpty())
            return null;

        Map<String, Double> results = new HashMap<String, Double>();
        int totalCount = 0, maleCount=0, femaleCount=0;
        for(SurveyEntity s : answers){
            String question66 = s.getQuestion66();
            if(question66 != null && !question66.isEmpty()) {
                if (question66.equals(SurveyStrings.statement_strongly_agree) || question66.equals(SurveyStrings.statement_agree)){
                    totalCount++;
//                    if(s.isMale()){
//                        maleCount++;
//                    }
//                    else{
//                        femaleCount++;
//                    }
                }
            }
        }

        if(totalCount > 0) {
            totalPercentage = (double)totalCount /(double) answers.size();
            totalPercentage = round(totalPercentage * 100, 0);

//            malePercentage = maleCount / totalCount;
//            female = femaleCount / totalCount;
        }


        results.put(ResponseKeys.ChartsController_Total_Percentage, totalPercentage );
//        results.put("male", malePercentage * 100);
//        results.put("female", female * 100);

        return results;
    }

    public static double round(double value, int places) {

        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
