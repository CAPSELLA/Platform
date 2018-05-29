package gr.uoa.di.madgik.publicfood.Utilities;


import gr.uoa.di.madgik.publicfood.facades.Facade;
import org.json.JSONException;
import org.json.JSONObject;

public class StatisticsResults extends Facade {

    private int id;
    private String name;
    private double result;
    private int type;
    private int questionType;

    private String domain;

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatisticsResults(){};

    public StatisticsResults(int id, String name , double result){
        this.id = id;
        this.name = name;
        this.result = result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Override
    public StatisticsResults Deserialize(Object payload) throws JSONException {

        JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);

        StatisticsResults statisticsResults = new StatisticsResults();

        statisticsResults.setId(jsonObject.getInt("id"));
        statisticsResults.setDomain(jsonObject.getString("domain"));
        statisticsResults.setQuestionType(jsonObject.getInt("questionType"));
        statisticsResults.setName(jsonObject.getString("name"));
        statisticsResults.setResult(jsonObject.getDouble("result"));
        statisticsResults.setType(jsonObject.getInt("type"));

        return statisticsResults;
    }

    public enum Type {
        SCHOOL(0), POST_CODE(1), AREA(2);

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public static Type valueOf(int x) {
            switch (x) {
                case 0:
                    return SCHOOL;
                case 1:
                    return POST_CODE;
                case 2:
                    return AREA;

            }
            return null;
        }

        public int getValue() {
            return id;
        }
    }

    public enum QuestionType {
        SnacksBetweenMeals(0), EatFruitProvidedBySchool(1), DontHaveTimeToCookVegetables(2), FruitsAvailableHome(3), SmoothiesAvailableHome(4);

        private final int id;

        QuestionType(int id) {
            this.id = id;
        }

        public static QuestionType valueOf(int x) {
            switch (x) {
                case 0:
                    return SnacksBetweenMeals;
                case 1:
                    return EatFruitProvidedBySchool;
                case 2:
                    return DontHaveTimeToCookVegetables;
                case 3:
                    return FruitsAvailableHome;
                case 4:
                    return SmoothiesAvailableHome;

            }
            return null;
        }

        public int getValue() {
            return id;
        }
    }

}
