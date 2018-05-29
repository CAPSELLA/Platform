package gr.uoa.di.madgik.publicfood.security.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;


public final class GoogleAuthHelper {


    private static final String CALLBACK_URI = "http://localhost:8080/auth";

    private static final Collection<String> SCOPES = Arrays.asList("email");

    private GoogleClientSecrets clientSecrets = null;
    // start google authentication constants
   // private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // end google authentication constants

    private String stateToken;

    private  GoogleAuthorizationCodeFlow flow;

    /**
     * Constructor initializes the Google Authorization Code Flow with CLIENT ID, SECRET, and SCOPE
     */
    public GoogleAuthHelper() {


        InputStream in =  GoogleAuthHelper.class.getResourceAsStream("/client_secret.json");
        try {
            clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).build();
        } catch (IOException e) {
            e.printStackTrace();
        }





        generateStateToken();
    }

    /**
     * Builds a login URL based on client ID, secret, callback URI, and scope
     */
    public String buildLoginUrl() {

        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();

        return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
    }

    /**
     * Generates a secure state token
     */
    private void generateStateToken(){

        SecureRandom sr1 = new SecureRandom();

        stateToken = "google;"+sr1.nextInt();
        buildLoginUrl();

    }

    /**
     * Accessor for state token
     */
    public String getStateToken(){
        return stateToken;
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for the user's profile information
     * @return JSON formatted user profile information
     * @param authCode authentication code provided by google
     */
    public String getUserInfoJson(final String authCode) throws IOException {

        final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
        final Credential credential = flow.createAndStoreCredential(response, null);
        final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
        // Make an authenticated request
        final GenericUrl url = new GenericUrl(""); //(USER_INFO_URL);
        final HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setContentType("application/json");
        final String jsonIdentity = request.execute().parseAsString();

        return jsonIdentity;

    }


    public String getAccessToken(String code) {
        try {
            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
                    HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets.getWeb().getClientId(),
                    clientSecrets.getWeb().getClientSecret(),
                    code,
                    clientSecrets.getDetails().getRedirectUris().get(0)).execute();

            String x = response.getAccessToken();
//            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),JacksonFactory.getDefaultInstance(),
//                    "https://www.googleapis.com/oauth2/v4/token",
//                    clientSecrets.getDetails().getClientId(),
//                    clientSecrets.getDetails().getClientSecret(),
//                    authCode, "postmessage")
//                    .execute();
            return response.getAccessToken();
        } catch (IOException e) {
            new RuntimeException("An unknown problem occured while retrieving token");
        }
        return null;
    }


}
