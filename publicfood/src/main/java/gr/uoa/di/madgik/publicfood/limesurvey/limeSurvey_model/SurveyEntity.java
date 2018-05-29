package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import gr.uoa.di.madgik.publicfood.facades.Facade;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Objects;


public class SurveyEntity extends Facade {

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion6() {
        return question6;
    }

    public void setQuestion6(String question6) {
        this.question6 = question6;
    }

    public String getQuestion32() {
        return question32;
    }

    public void setQuestion32(String question32) {
        this.question32 = question32;
    }

    public String getQuestion33() {
        return question33;
    }

    public void setQuestion33(String question33) {
        this.question33 = question33;
    }

    public String getQuestion34() {
        return question34;
    }

    public void setQuestion34(String question34) {
        this.question34 = question34;
    }

    public String getQuestion35() {
        return question35;
    }

    public void setQuestion35(String question35) {
        this.question35 = question35;
    }

    public String getQuestion36() {
        return question36;
    }

    public void setQuestion36(String question36) {
        this.question36 = question36;
    }

    public String getQuestion37() {
        return question37;
    }

    public void setQuestion37(String question37) {
        this.question37 = question37;
    }

    public String getQuestion38() {
        return question38;
    }

    public void setQuestion38(String question38) {
        this.question38 = question38;
    }

    public String getQuestion39() {
        return question39;
    }

    public void setQuestion39(String question39) {
        this.question39 = question39;
    }

    public String getQuestion40() {
        return question40;
    }

    public void setQuestion40(String question40) {
        this.question40 = question40;
    }

    public String getQuestion41() {
        return question41;
    }

    public void setQuestion41(String question41) {
        this.question41 = question41;
    }

    public String getQuestion42() {
        return question42;
    }

    public void setQuestion42(String question42) {
        this.question42 = question42;
    }

    public String getQuestion43() {
        return question43;
    }

    public void setQuestion43(String question43) {
        this.question43 = question43;
    }

    public String getQuestion44() {
        return question44;
    }

    public void setQuestion44(String question44) {
        this.question44 = question44;
    }

    public String getQuestion45() {
        return question45;
    }

    public void setQuestion45(String question45) {
        this.question45 = question45;
    }

    public String getQuestion46() {
        return question46;
    }

    public void setQuestion46(String question46) {
        this.question46 = question46;
    }

    public String getQuestion47() {
        return question47;
    }

    public void setQuestion47(String question47) {
        this.question47 = question47;
    }

    public String getQuestion48() {
        return question48;
    }

    public void setQuestion48(String question48) {
        this.question48 = question48;
    }

    public String getQuestion49() {
        return question49;
    }

    public void setQuestion49(String question49) {
        this.question49 = question49;
    }

    public String getQuestion50() {
        return question50;
    }

    public void setQuestion50(String question50) {
        this.question50 = question50;
    }

    public String getQuestion51() {
        return question51;
    }

    public void setQuestion51(String question51) {
        this.question51 = question51;
    }

    public String getQuestion52() {
        return question52;
    }

    public void setQuestion52(String question52) {
        this.question52 = question52;
    }

    public String getQuestion53() {
        return question53;
    }

    public void setQuestion53(String question53) {
        this.question53 = question53;
    }

    public String getQuestion54() {
        return question54;
    }

    public void setQuestion54(String question54) {
        this.question54 = question54;
    }

    public String getQuestion55() {
        return question55;
    }

    public void setQuestion55(String question55) {
        this.question55 = question55;
    }

    public String getQuestion56() {
        return question56;
    }

    public void setQuestion56(String question56) {
        this.question56 = question56;
    }

    public String getQuestion73() {
        return question73;
    }

    public void setQuestion73(String question73) {
        this.question73 = question73;
    }

    public String getQuestion74() {
        return question74;
    }

    public void setQuestion74(String question74) {
        this.question74 = question74;
    }

    public String getQuestion66() {
        return question66;
    }

    public void setQuestion66(String question66) {
        this.question66 = question66;
    }

    public String getQuestion58() {
        return question58;
    }

    public void setQuestion58(String question58) {
        this.question58 = question58;
    }

    public String getQuestion60() {
        return question60;
    }

    public void setQuestion60(String question60) {
        this.question60 = question60;
    }

    public String getQuestion62() {
        return question62;
    }

    public void setQuestion62(String question62) {
        this.question62 = question62;
    }

    public String getQuestion63() {
        return question63;
    }

    public void setQuestion63(String question63) {
        this.question63 = question63;
    }

    private String question3;
    private String question6;
    private String question32;
    private String question33;
    private String question34;
    private String question35;
    private String question36;
    private String question37;
    private String question38;
    private String question39;
    private String question40;
    private String question41;
    private String question42;
    private String question43;
    private String question44;
    private String question45;
    private String question46;

    private String question47;
    private String question48;
    private String question49;
    private String question50;
    private String question51;
    private String question52;
    private String question53;
    private String question54;
    private String question55;

    private String question56;

    private String question73;

    private String question74;

    private String question66;
    private String question58;
    private String question60;
    private String question62;
    private String question63;







    @JsonIgnore
    private boolean isMale = false;

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Override
    public SurveyEntity Deserialize(Object payload) throws JSONException {

        SurveyEntity surveyEntity = new SurveyEntity();
        JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);
        JSONArray result = jsonObject.getJSONArray("responses");
     //   System.out.println(result.length());
        if(result.length() > 0) {

            JSONObject explrObject = result.getJSONObject(0);
            Iterator<?> keys = explrObject.keys();
            JSONObject surveyAnswers = null;
            while( keys.hasNext() ) {
                String key = (String) keys.next();
//                System.out.println("Key: " + key);
//                System.out.println("Value: " + explrObject.getJSONObject(key));
                surveyAnswers = explrObject.getJSONObject(key);
            }
            if(surveyAnswers != null) {
                String question6 = surveyAnswers.getString("C1[SQ001]");
                surveyEntity.setQuestion6(question6);

                String question3 = surveyAnswers.getString("B1[SQ001]");
                surveyEntity.setQuestion3(question3);

                String question32 = surveyAnswers.getString("Z112[SQ001]");
                surveyEntity.setQuestion32(question32);
                String question38 = surveyAnswers.getString("Z112[SQ007]");
                surveyEntity.setQuestion38(question38);
                String question40 = surveyAnswers.getString("Z112[SQ010]");
                surveyEntity.setQuestion40(question40);
                String question43 = surveyAnswers.getString("Z112[SQ013]");
                surveyEntity.setQuestion43(question43);
                String question34 = surveyAnswers.getString("Z112[SQ003]");
                surveyEntity.setQuestion34(question34);
                String question35 = surveyAnswers.getString("Z112[SQ004]");
                surveyEntity.setQuestion35(question35);
                String question37 = surveyAnswers.getString("Z112[SQ006]");
                surveyEntity.setQuestion37(question37);
                String question39 = surveyAnswers.getString("Z112[SQ009]");
                surveyEntity.setQuestion39(question39);
                String question41 = surveyAnswers.getString("Z112[SQ011]");
                surveyEntity.setQuestion41(question41);
                String question33 = surveyAnswers.getString("Z112[SQ002]");
                surveyEntity.setQuestion33(question33);

                String question74 = surveyAnswers.getString("Z119");
                surveyEntity.setQuestion74(question74);

                String question73 = surveyAnswers.getString("Z115");
                surveyEntity.setQuestion73(question73);

                String question47 = surveyAnswers.getString("Z114[SQ001]");
                surveyEntity.setQuestion47(question47);
                String question48 = surveyAnswers.getString("Z114[SQ002]");
                surveyEntity.setQuestion48(question48);
                String question49 = surveyAnswers.getString("Z114[SQ003]");
                surveyEntity.setQuestion49(question49);
                String question50 = surveyAnswers.getString("Z114[SQ004]");
                surveyEntity.setQuestion50(question50);
                String question51 = surveyAnswers.getString("Z114[SQ005]");
                surveyEntity.setQuestion51(question51);
                String question52 = surveyAnswers.getString("Z114[SQ006]");
                surveyEntity.setQuestion52(question52);
                String question53 = surveyAnswers.getString("Z114[SQ007]");
                surveyEntity.setQuestion53(question53);
                String question54 = surveyAnswers.getString("Z114[SQ008]");
                surveyEntity.setQuestion54(question54);
                String question55 = surveyAnswers.getString("Z114[SQ009]");
                surveyEntity.setQuestion55(question55);

                String question46 = surveyAnswers.getString("Z113");
                surveyEntity.setQuestion46(question46);

                String question56 = surveyAnswers.getString("Z116");
                surveyEntity.setQuestion56(question56);

                String question66 = surveyAnswers.getString("Q[SQ012]");
                surveyEntity.setQuestion66(question66);

                String question58 = surveyAnswers.getString("Q[SQ002]");
                surveyEntity.setQuestion58(question58);

                String question60 = surveyAnswers.getString("Q[SQ004]");
                surveyEntity.setQuestion60(question60);

                String question62 = surveyAnswers.getString("Q[SQ006]");
                surveyEntity.setQuestion62(question62);
                String question63 = surveyAnswers.getString("Q[SQ007]");
                surveyEntity.setQuestion63(question63);


                return surveyEntity;
            }

        }
        return null;
    }

    public enum Criteria {
        SCHOOL(0), CITY(1), AREA(2), POSTCODE(3);

        private final int id;

        Criteria(int id) {
            this.id = id;
        }

        public static Criteria valueOf(int x) {
            switch (x) {
                case 0:
                    return SCHOOL;
                case 1:
                    return CITY;
                case 2:
                    return AREA;
                case 3:
                    return POSTCODE;

            }
            return null;
        }

        public int getValue() {
            return id;
        }
    }

}
