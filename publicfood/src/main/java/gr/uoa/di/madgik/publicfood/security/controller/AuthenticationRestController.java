package gr.uoa.di.madgik.publicfood.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import gr.uoa.di.madgik.publicfood.Utilities.HttpRequestUtil;
import gr.uoa.di.madgik.publicfood.capsella_authentication_service.User;

import gr.uoa.di.madgik.publicfood.limesurvey.utilities.Utils;
import gr.uoa.di.madgik.publicfood.model.DomainEntity;
import gr.uoa.di.madgik.publicfood.model.Provider;
import gr.uoa.di.madgik.publicfood.model.UserEntity;
import gr.uoa.di.madgik.publicfood.model.WeeklyQuestionnaireEntity;
import gr.uoa.di.madgik.publicfood.security.JwtTokenUtil;
import gr.uoa.di.madgik.publicfood.security.repository.DomainRepository;
import gr.uoa.di.madgik.publicfood.security.repository.UserRepository;
import gr.uoa.di.madgik.publicfood.security.repository.WeeklyQuestionnaireRepository;
import gr.uoa.di.madgik.publicfood.security.service.JwtAuthenticationResponse;
import gr.uoa.di.madgik.publicfood.security.service.JwtUserDetailsServiceImpl;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Transactional
public class AuthenticationRestController {


    private static final Collection<String> SCOPES = Arrays.asList("");
    private static final String USERINFO_ENDPOINT
            = "https://www.googleapis.com/plus/v1/people/me";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${spring.social.google.appId}")
    private String googleClientId;

    @Value("${spring.social.google.appSecret}")
    private String googleClientSecret;

    @Value("${spring.social.linkedin.appId}")
    private String linkedInClientId;

    @Value("${spring.social.linkedin.appSecret}")
    private String linkedInclientSecret;

    @Value("${provider.linkedin.redirect.uri}")
    private String linkedInRedirectUri;

    @Value("${provider.linkedin.profile}")
    private String linkedInProfileUrl;

    @Value("${spring.social.facebook.appId}")
    private String facebookClientId;

    @Value("${spring.social.facebook.appSecret}")
    private String facebookClientSecret;
    @Value("${provider.facebook.redirect.uri}")
    private String facebookRedirectUri;

    @Value("${authorization.server.url}")
    private String authenticationServerUrl;
    @Value("${authorization.server.authenticate}")
    private String authenticateUrl;

    @Value("${authorization.server.changePassword}")
    private String changePasswordUrl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private WeeklyQuestionnaireRepository weeklyQuestionnaireRepository;



    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JavaMailSender emailSender;

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public AuthenticationRestController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }
    @RequestMapping(value = "/auth/sendMailTest", method = RequestMethod.GET)
    public ResponseEntity<?> limesurveyTest(HttpServletRequest request) throws Exception
    {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("social@publicfood.eu");
        message.setTo("cmitatakis@gmail.com");
        message.setSubject("Test");
        message.setText("Dear Christos \n \n" +
                "the public food team welcomes you!");
        emailSender.send(message);

        //List<ParticipantEntity> all = participantRepository.findAll();

     //   Utils.getAnswerByToken("cFBZc");
        return new ResponseEntity<>( HttpStatus.OK);

    }

    @RequestMapping(value = "/auth/facebook", method = RequestMethod.POST)
    public ResponseEntity<?> facebookAuthorization(@RequestBody String tokenResponse, HttpServletRequest request) throws Exception
    {
        JSONObject jsonObject = new JSONObject(tokenResponse);
        String code = jsonObject.getString("tokenResponse");
        String domain = request.getHeader("Domain");

        // retrieve app access token
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://graph.facebook.com/v2.5/oauth/access_token?&client_id=" + facebookClientId +"&redirect_uri="+ facebookRedirectUri + "&client_secret=" + facebookClientSecret + "&code=" +code,  String.class);
        JSONObject token = new JSONObject(result);

        restTemplate = new RestTemplate();
        String t = token.getString("access_token");

        String resultProfile = restTemplate.getForObject("https://graph.facebook.com/v2.5/me?fields=id,email,first_name,last_name,link,name,picture.type(large)&access_token=" + token.getString("access_token") ,  String.class);
        JSONObject userDetails = new JSONObject(resultProfile);

        String jwtToken;
        String firstName = userDetails.getString("first_name");
        String lastName = userDetails.getString("last_name");
        String emailAddress = userDetails.getString("email");
        String providerUUID = userDetails.getString("id");

        JSONObject picture = (JSONObject) userDetails.get("picture") ;
        JSONObject data = (JSONObject) picture.get("data");
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.Facebook.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if(user == null ) {
            user = new UserEntity();
            user.setEmail(emailAddress);
            user.setDisplayname(firstName + " " + lastName);
            user.setToken(t);
            user.setProviderid(Provider.Facebook.getValue());
            user.setProvider(Provider.Facebook.toString());
            user.setProviderUuid(providerUUID);
            user.setPicture(data.getString("url"));
            user.setStatus(UserEntity.Status.SIGNED_IN.getValue());
            user.setDomainName(domain);

            Integer lastID = userRepository.getLastID();
            if(lastID == null)
                lastID = 1;
            else
                lastID++;
            user.setId(lastID.longValue());
            UserEntity save = userRepository.saveAndFlush(user);


            System.out.println(save.getId());

//            ParticipantEntity participantEntity = Utils.createParticipantEntity(save);
//            participantRepository.save(participantEntity);

            try {
                UserEntity participantEntity = Utils.createParticipantEntity(save);
                if(participantEntity != null){
                    userRepository.saveAndFlush(participantEntity);
                }
                else{
                    return new ResponseEntity<String>("Registration Error", HttpStatus.BAD_REQUEST);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
            weeklyQuestionnaireEntity.setUserId(String.valueOf(lastID));
            Timestamp timestamp = new Timestamp(date1.getTime().getTime());
            weeklyQuestionnaireEntity.setDeadline(timestamp);

            weeklyQuestionnaireRepository.save(weeklyQuestionnaireEntity);

            // Reload password post-security so we can generate token
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Facebook.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", jwtUser.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));

        }
        else
        {
            responseMap.put("token", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.EXISTED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

    }


    @RequestMapping(value = "/login/facebook", method = RequestMethod.POST)
    public ResponseEntity<?> facebookLogin(@RequestBody String tokenResponse, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject(tokenResponse);
        String code = jsonObject.getString("tokenResponse");
        System.out.println("Facebook code: " + tokenResponse);
        String domain = request.getHeader("Domain");

        // retrieve app access token
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://graph.facebook.com/v2.5/oauth/access_token?&client_id=" + facebookClientId + "&redirect_uri=" + facebookRedirectUri + "&client_secret=" + facebookClientSecret + "&code=" + code, String.class);
        JSONObject token = new JSONObject(result);

        restTemplate = new RestTemplate();
        String t = token.getString("access_token");

        String resultProfile = restTemplate.getForObject("https://graph.facebook.com/v2.5/me?fields=id,email,first_name,last_name,link,name,picture.type(large)&access_token=" + token.getString("access_token"), String.class);
        JSONObject userDetails = new JSONObject(resultProfile);

        String jwtToken;
        String firstName = userDetails.getString("first_name");
        String lastName = userDetails.getString("last_name");
        String emailAddress = userDetails.getString("email");
        String providerUUID = userDetails.getString("id");

        JSONObject picture = (JSONObject) userDetails.get("picture");
        JSONObject data = (JSONObject) picture.get("data");
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.Facebook.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if (user == null) {
            responseMap.put("token", null);
            responseMap.put("user", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.UNAUTHORIZED.getValue()));

        }
        else if(user.getStatus() == UserEntity.Status.SIGNED_IN.getValue())
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Facebook.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));
        }
        else if(user.getStatus() == UserEntity.Status.REGISTERED.getValue())
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Facebook.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.REGISTERED.getValue()));
        }
        else
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Facebook.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.FULLY_REGISTERED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/linkedin", method = RequestMethod.POST)
    public ResponseEntity<?> linkedinAuthorization(@RequestBody String tokenResponse, HttpServletRequest request) throws Exception
    {

        String domain = request.getHeader("Domain");

        JSONObject jsonObject = new JSONObject(tokenResponse);
        String code = jsonObject.getString("tokenResponse");

            OAuthClientRequest req = OAuthClientRequest.tokenLocation("https://www.linkedin.com/uas/oauth2/accessToken")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setCode(code)
                .setRedirectURI(linkedInRedirectUri)
                .setClientId(linkedInClientId)
                .setClientSecret(linkedInclientSecret).buildQueryMessage();


        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthAccessTokenResponse oAuth2Response2 = oAuthClient.accessToken(req, OAuthJSONAccessTokenResponse.class);
        String accessToken = oAuth2Response2.getAccessToken();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> response = restTemplate.exchange(linkedInProfileUrl + "?oauth2_access_token=" + accessToken+ "&format=json",
                HttpMethod.GET,
                entity,
                String.class

        );

        String res = response.getBody();
        JSONObject userDetails = new JSONObject(res);
        String jwtToken;
        String firstName = userDetails.getString("firstName");
        String lastName = userDetails.getString("lastName");
        String emailAddress = userDetails.getString("emailAddress");
        String providerUUID = userDetails.getString("id");
        String pictureUrl = "";
        if(userDetails.has("pictureUrl"))
             pictureUrl = userDetails.getString("pictureUrl");
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.LinkedIn.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if(user == null ) {
            user = new UserEntity();
            user.setEmail(emailAddress);
            user.setDisplayname(firstName + " " + lastName);
            user.setToken(accessToken);
            user.setProviderid(Provider.LinkedIn.getValue());
            user.setProvider(Provider.LinkedIn.toString());
            user.setProviderUuid(providerUUID);
            user.setPicture(pictureUrl);
            user.setStatus(UserEntity.Status.SIGNED_IN.getValue());
            user.setDomainName(domain);

            Integer lastID = userRepository.getLastID();
            if(lastID == null)
                lastID = 1;
            else
                lastID++;
            user.setId(lastID.longValue());
            UserEntity save = userRepository.saveAndFlush(user);


            System.out.println(save.getId());

//            ParticipantEntity participantEntity = Utils.createParticipantEntity(save);
//            participantRepository.save(participantEntity);

            try {
                UserEntity participantEntity = Utils.createParticipantEntity(save);
                if(participantEntity != null){
                    userRepository.saveAndFlush(participantEntity);
                }
                else{
                    return new ResponseEntity<String>("Registration Error", HttpStatus.BAD_REQUEST);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
            weeklyQuestionnaireEntity.setUserId(String.valueOf(lastID));
            Timestamp timestamp = new Timestamp(date1.getTime().getTime());
            weeklyQuestionnaireEntity.setDeadline(timestamp);

            weeklyQuestionnaireRepository.save(weeklyQuestionnaireEntity);



            // Reload password post-security so we can generate token
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.LinkedIn.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", jwtUser.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));

        }
        else
        {
            responseMap.put("token", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.EXISTED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }


    @RequestMapping(value = "/login/linkedin", method = RequestMethod.POST)
    public ResponseEntity<?> linkedinLogin(@RequestBody String tokenResponse, HttpServletRequest request) throws Exception {

        JSONObject jsonObject = new JSONObject(tokenResponse);
        String code = jsonObject.getString("tokenResponse");
        String domain = request.getHeader("Domain");

        OAuthClientRequest req = OAuthClientRequest.tokenLocation("https://www.linkedin.com/uas/oauth2/accessToken")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setCode(code)
                .setRedirectURI(linkedInRedirectUri)
                .setClientId(linkedInClientId)
                .setClientSecret(linkedInclientSecret).buildQueryMessage();


        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthAccessTokenResponse oAuth2Response2 = oAuthClient.accessToken(req, OAuthJSONAccessTokenResponse.class);
        String accessToken = oAuth2Response2.getAccessToken();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> response = restTemplate.exchange(linkedInProfileUrl + "?oauth2_access_token=" + accessToken + "&format=json",
                HttpMethod.GET,
                entity,
                String.class

        );

        String res = response.getBody();
        JSONObject userDetails = new JSONObject(res);
        String jwtToken;
        String firstName = userDetails.getString("firstName");
        String lastName = userDetails.getString("lastName");
        String emailAddress = userDetails.getString("emailAddress");
        String providerUUID = userDetails.getString("id");
        String pictureUrl = "";
        if (userDetails.has("pictureUrl"))
            pictureUrl = userDetails.getString("pictureUrl");
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.LinkedIn.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if (user == null) {
            responseMap.put("token", null);
            responseMap.put("user", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.UNAUTHORIZED.getValue()));
        }

        else if(user.getStatus() == UserEntity.Status.SIGNED_IN.getValue())
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.LinkedIn.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));
        }
        else if(UserEntity.Status.valueOf(user.getStatus()) == UserEntity.Status.REGISTERED)
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.LinkedIn.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.REGISTERED.getValue()));
        }
        else
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.LinkedIn.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.FULLY_REGISTERED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);

    }

    @RequestMapping(value = "/domain/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getProfile(HttpServletRequest request)  {

        int providerId;
        String domain = request.getHeader("Domain");
        String authToken = request.getHeader(this.tokenHeader);
        providerId = Integer.parseInt(request.getHeader("providerId"));
        if(authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }

        String providerUUID = jwtTokenUtil.getProviderUUIDFromToken(authToken);
        UserEntity user = this.jwtUserDetailsService.loadUserByID(providerUUID, providerId, domain);
        return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/login/google", method = RequestMethod.POST)
    public ResponseEntity<?> googleLogin(@RequestBody String tokenResponse, HttpServletRequest request)   throws IOException{

        String domain = request.getHeader("Domain");

        JSONObject jsonObject = new JSONObject(tokenResponse);
        String query = jsonObject.getString("tokenResponse");
        Map<String, String> params = HttpRequestUtil.splitQuery(query);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(getClass().getResourceAsStream("/client_secret.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)

                .build();
        TokenResponse token = new TokenResponse();
        token.setAccessToken(params.get("access_token"));
        token.setTokenType(params.get("token_type"));
        token.setExpiresInSeconds(Long.valueOf(params.get("expires_in")));
        Credential credential = flow.createAndStoreCredential(token, null);
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
        GenericUrl url = new GenericUrl(USERINFO_ENDPOINT);      // Make an authenticated request.
        HttpRequest req = requestFactory.buildGetRequest(url);
        req.getHeaders().setContentType("application/json");
        String jsonIdentity, jwtToken;
        jsonIdentity = req.execute().parseAsString();
        JSONObject profile = new JSONObject(jsonIdentity);
        JSONArray emails =  (JSONArray) (profile.get("emails"));
        JSONObject image = profile.getJSONObject("image");
        JSONObject email = emails.getJSONObject(0);
        String providerUUID = profile.getString("id");

        @SuppressWarnings("unchecked")
        HashMap<String, String> userIdResult = new ObjectMapper().readValue(jsonIdentity, HashMap.class);
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.Google.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if(user == null ) {

            responseMap.put("token", null);
            responseMap.put("user", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.UNAUTHORIZED.getValue()));

        }
        else if(user.getStatus() == UserEntity.Status.SIGNED_IN.getValue())
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Google.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());

            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));
        }
        else if(user.getStatus() == UserEntity.Status.REGISTERED.getValue())
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Google.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.REGISTERED.getValue()));
        }
        else
        {
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Google.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", user.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.FULLY_REGISTERED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }


    @RequestMapping(value = "/auth/google", method = RequestMethod.POST)
    public ResponseEntity<?> googleAuthorization(@RequestBody String tokenResponse, HttpServletRequest request) throws IOException {

        String domain = request.getHeader("Domain");

        JSONObject jsonObject = new JSONObject(tokenResponse);
        String query = jsonObject.getString("tokenResponse");
        Map<String, String> params = HttpRequestUtil.splitQuery(query);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(getClass().getResourceAsStream("/client_secret.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)

                .build();
        TokenResponse token = new TokenResponse();
        token.setAccessToken(params.get("access_token"));
        token.setTokenType(params.get("token_type"));
        token.setExpiresInSeconds(Long.valueOf(params.get("expires_in")));
        Credential credential = flow.createAndStoreCredential(token, null);
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
        GenericUrl url = new GenericUrl(USERINFO_ENDPOINT);      // Make an authenticated request.
        HttpRequest req = requestFactory.buildGetRequest(url);
        req.getHeaders().setContentType("application/json");
        String jsonIdentity, jwtToken;
        jsonIdentity = req.execute().parseAsString();
        JSONObject profile = new JSONObject(jsonIdentity);
        JSONArray emails =  (JSONArray) (profile.get("emails"));
        JSONObject image = profile.getJSONObject("image");
        JSONObject email = emails.getJSONObject(0);

        String providerUUID = profile.getString("id");
        @SuppressWarnings("unchecked")
        HashMap<String, String> userIdResult = new ObjectMapper().readValue(jsonIdentity, HashMap.class);
        UserEntity user = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, Provider.Google.getValue(), domain);
        Map<String, String> responseMap = new HashMap<String, String>();
        UserEntity jwtUser;
        if(user == null ) {
            user = new UserEntity();
            user.setEmail(email.getString("value"));
            user.setDisplayname(userIdResult.get("displayName"));
            user.setToken(token.getAccessToken());
            user.setProviderid(Provider.Google.getValue());
            user.setProvider(Provider.Google.toString());
            user.setPicture(image.getString("url"));
            user.setProviderUuid(providerUUID);
            user.setStatus(UserEntity.Status.SIGNED_IN.getValue());
            user.setDomainName(domain);

            Integer lastID = userRepository.getLastID();
            if(lastID == null)
                lastID = 1;
            else
                lastID++;
            user.setId(lastID.longValue());
            UserEntity save = userRepository.saveAndFlush(user);


            System.out.println(save.getId());

//            ParticipantEntity participantEntity = Utils.createParticipantEntity(save);
//            participantRepository.save(participantEntity);
            try {
                UserEntity participantEntity = Utils.createParticipantEntity(save);
                if(participantEntity != null){
                    userRepository.saveAndFlush(participantEntity);
                }
                else{
                    return new ResponseEntity<String>("Registration Error", HttpStatus.BAD_REQUEST);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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
            weeklyQuestionnaireEntity.setUserId(String.valueOf(lastID));
            Timestamp timestamp = new Timestamp(date1.getTime().getTime());
            weeklyQuestionnaireEntity.setDeadline(timestamp);

            weeklyQuestionnaireRepository.save(weeklyQuestionnaireEntity);
            // Reload password post-security so we can generate token
            jwtUser = jwtUserDetailsService.loadUserByID(providerUUID, Provider.Google.getValue(), domain);
            jwtToken = jwtTokenUtil.generateToken(jwtUser);

            responseMap.put("token", jwtToken);
            responseMap.put("user", jwtUser.Serialize().toString());
            responseMap.put("status", String.valueOf(UserEntity.Status.SIGNED_IN.getValue()));

        }
        else
        {
            responseMap.put("token", null);
            responseMap.put("status", String.valueOf(UserEntity.Status.EXISTED.getValue()));
        }

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String providerUUID = jwtTokenUtil.getProviderUUIDFromToken(token);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.path}"+ "/" +"${jwt.route.authentication.domain}", method = RequestMethod.GET)
    public ResponseEntity<?> domainValidation(HttpServletRequest request, @RequestParam String domain) {
        Map<String, Boolean> responseMap = new HashMap<String, Boolean>();

        final DomainEntity domainEntity = domainRepository.findByName(domain);
        if(domainEntity != null )
            responseMap.put("domain", true);
        else
            responseMap.put("domain", false);

        return new ResponseEntity<Map<String, Boolean>>(responseMap, HttpStatus.OK);

    }

    @RequestMapping(value = "auth/limesurvey/addparticipant", method = RequestMethod.GET)
    public ResponseEntity<?> addParticipant() {
        Map<String, Boolean> responseMap = new HashMap<String, Boolean>();

        UserEntity test = new UserEntity();
        test.setProviderUuid("AEK");
        test.setDisplayname("test");
        test.setEmail("test@gmail.com");

        try {
            UserEntity participantEntity = Utils.createParticipantEntity(test);
            if(participantEntity != null){
                //save
            }
            else{
                //limesurvey error
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Map<String, Boolean>>(responseMap, HttpStatus.OK);

    }

    @RequestMapping(value = "/ldapLogin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login( @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("domain") String domain) {

        String response;
        String url = authenticationServerUrl + authenticateUrl;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> responseMap = new HashMap<String, Object>();

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("username", username).queryParam("password", password);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            String body = result.getBody();
            JSONObject jsonObject = new JSONObject(body);
            String token = (String) jsonObject.get("token");

            User user = new User().Deserialize(jsonObject.get("user"));
            if(user.validateDomain(domain)) {
                String jwtToken = jwtTokenUtil.generateToken(token);

                responseMap.put("token", jwtToken);
                responseMap.put("user", user);

                return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

            }
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

        }


    }


    @RequestMapping(value = "/auth/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> changePassword( @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {

        String response;
        String url = authenticationServerUrl + changePasswordUrl;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> responseMap = new HashMap<String, Object>();

//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("username", username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("username", username).queryParam("password", password).queryParam("newPassword", newPassword);

        System.out.println(builder.build().encode().toUri().toString());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            String body = result.getBody();
            if(result.getStatusCode() == HttpStatus.OK){
                responseMap.put("status", 1);
                responseMap.put("response","Password updated");
                return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);

            }
            else
            {
                responseMap.put("status", 0);
                responseMap.put("response","Wrong password");
                return new ResponseEntity<Map<String, Object>>(responseMap,HttpStatus.NOT_FOUND);

            }
        } catch (HttpStatusCodeException exception) {
            responseMap.put("status", 0);
            responseMap.put("response","Wrong password");
            return new ResponseEntity<Map<String, Object>>(responseMap,HttpStatus.NOT_FOUND);

        }


    }

}
