package gr.uoa.di.madgik.publicfood.limesurvey.utilities;

import com.github.falydoor.limesurveyrc.LimesurveyRC;
import com.github.falydoor.limesurveyrc.dto.LsApiBody;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gr.uoa.di.madgik.publicfood.capsella_authentication_service.User;
import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.ParticipantEntity;
import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.SurveyEntity;
import gr.uoa.di.madgik.publicfood.model.UserEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Utils {

    public static String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        String result = jobject.get("result").getAsString();
        return result;
    }
    public static UserEntity createParticipantEntity(UserEntity userEntity) throws UnsupportedEncodingException
    {

        JSONObject newParticipant = new JSONObject();
        newParticipant.put("emailStatus", "ok");
        newParticipant.put("firstname", userEntity.getDisplayname());
        newParticipant.put("email", userEntity.getEmail());
       // newParticipant.put("token", userEntity.getProviderUuid());

        DefaultHttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost("https://publicfood.eu/limesurvey/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"****\", \"*****\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\"method\": \"add_participants\", \"params\": [ \""+sessionKey+"\", \"554517\", [" + newParticipant.toString() +"] ], \"id\": 1}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String r = EntityUtils.toString(entity);
                    System.out.println(r);
                    JSONObject res = new JSONObject(r);
                    JSONArray result = res.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject explrObject = result.getJSONObject(i);
                        String registrationToken = explrObject.getString("token");
                        System.out.println(registrationToken);
                        userEntity.setLimeSurveyRegistrationToken(registrationToken);
                        break;
                       // return userEntity;
                    }
                  //  result.g
                 //   JSONObject jsonObject = result.getJSONObject(0);
                }

                post.setEntity( new StringEntity("{\"method\": \"add_participants\", \"params\": [ \""+sessionKey+"\", \"376212\", [" + newParticipant.toString() +"] ], \"id\": 1}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String r = EntityUtils.toString(entity);
                    System.out.println(r);
                    JSONObject res = new JSONObject(r);
                    JSONArray result = res.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject explrObject = result.getJSONObject(i);
                        String weeklyRegistrationToken = explrObject.getString("token");
                        System.out.println(weeklyRegistrationToken);
                        userEntity.setLimeSurveyWeeklyToken(weeklyRegistrationToken);

                        return userEntity;
                    }
                    //  result.g
                    //   JSONObject jsonObject = result.getJSONObject(0);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

//        ParticipantEntity participantEntity = new ParticipantEntity();
//
//        participantEntity.setEmail(userEntity.getEmail());
//        participantEntity.setFirstname(userEntity.getDisplayname());
//        participantEntity.setToken(userEntity.getProviderUuid());
//        participantEntity.setLastname("");
//        participantEntity.setEmailstatus("OK");
//        participantEntity.setSent("N");
//        participantEntity.setRemindersent("N");
//        participantEntity.setRemindercount(0);
//        participantEntity.setUsesleft(1);
//        participantEntity.setCompleted("N");
//        participantEntity.setTid(Math.toIntExact(userEntity.getId()));
//
//        return participantEntity;
        return null;
    }

    public static SurveyEntity getAnswerByToken(String token) throws UnsupportedEncodingException
    {
        DefaultHttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost("https://publicfood.eu/limesurvey/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"*****\", \"*****\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\"method\": \"export_responses_by_token\", \"params\": [ \""+sessionKey+"\", \"554517\", \"json\", \"" + token +"\", \"en\", \"complete\" ], \"id\": 1}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String r = EntityUtils.toString(entity);
                    JSONObject res = new JSONObject(r);
               //     System.out.println(r);
                    Object result1 = res.get("result");
                    if(result1 instanceof String) {
                        String result = res.getString("result");
                      //  System.out.println(result);

                        byte[] decodedBytes = Base64.getDecoder().decode(result);
                        String decodedString = new String(decodedBytes);

//                        System.out.println(r);
//                        System.out.println(decodedString);
                        SurveyEntity surveyEntity = new SurveyEntity().Deserialize(decodedString);
                        return surveyEntity;
                    }
                    else{
                   //     System.out.println("No results");
                    }

//                    JSONObject res = new JSONObject(r);
//                    JSONArray result = res.getJSONArray("result");
//                    for (int i = 0; i < result.length(); i++) {
//                        JSONObject explrObject = result.getJSONObject(i);
//                        String registrationToken = explrObject.getString("token");
//                        System.out.println(registrationToken);
//                       // userEntity.setLimeSurveyRegistrationToken(registrationToken);
//
//                    }
                    //  result.g
                    //   JSONObject jsonObject = result.getJSONObject(0);


                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
