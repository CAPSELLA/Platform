package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.facades.ChildCollectionFacade;
import gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model.SurveyEntity;
import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.security.JwtTokenUtil;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import gr.uoa.di.madgik.publicfood.security.repository.*;
import gr.uoa.di.madgik.publicfood.security.service.JwtUserDetailsServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DomainController {

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private AllergyRepository allergenRepository;

    @Autowired
    private PostCodeRepository postCodeRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private TranslationAllergyRepository translationAllergyRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    CookingCenterRepository cookingCenterRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    UserHasChildrenRepository userHasChildrenRepository;

    @Autowired
    ChildrenHasAllergyRepository childrenHasAllergyRepository;

    @Autowired
    public JavaMailSender emailSender;

    @Value("${jwt.header}")
    private String tokenHeader;

    @RequestMapping(value = "/domain/allergens", method = RequestMethod.GET)
    public ResponseEntity<?> getAllergens(HttpServletRequest request, @RequestParam String domain, @RequestParam String language)  {

        List<TranslationAllergyEntity> translatedAllergyEntities = translationAllergyRepository.findByAllergyDomainNameAndLanguage(domain, language);

        return new ResponseEntity<List<TranslationAllergyEntity>>(translatedAllergyEntities, HttpStatus.OK);
    }

    @RequestMapping(value = "/domain/areas", method = RequestMethod.GET)
    public ResponseEntity<?> getAreas(HttpServletRequest request, @RequestParam String domain)  {

        List<AreaEntity> areas = areaRepository.findByDomainName(domain);

        return new ResponseEntity<List<AreaEntity>>(areas, HttpStatus.OK);
    }

    @RequestMapping(value = "/domain/postCodes", method = RequestMethod.GET)
    public ResponseEntity<?> getPostCodes(HttpServletRequest request, @RequestParam String domain)  {

        List<PostCodeEntity> postCodes = postCodeRepository.findByDomainName(domain);

        return new ResponseEntity<List<PostCodeEntity>>(postCodes, HttpStatus.OK);
    }

    @RequestMapping(value = "/domain/schoolsWithCriteria", method = RequestMethod.GET)
    public ResponseEntity<?> getSchools(HttpServletRequest request, @RequestParam String domain, @RequestParam int criteria, @RequestParam int criteriaValue)  {

        if(SurveyEntity.Criteria.SCHOOL.getValue() == criteria) {
            SchoolEntity school = schoolRepository.findByIdAndDomainName(criteriaValue, domain);

            return new ResponseEntity<SchoolEntity>(school, HttpStatus.OK);
        }
        else if(SurveyEntity.Criteria.CITY.getValue() == criteria) {
            List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);

            return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
        }
        else if(SurveyEntity.Criteria.POSTCODE.getValue() == criteria) {
            List<SchoolEntity> schools = schoolRepository.findByDomainNameAndPostCodeIdPostCode(domain, criteriaValue);

            return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
        }
        else if(SurveyEntity.Criteria.AREA.getValue() == criteria) {
            List<SchoolEntity> schools = new ArrayList<SchoolEntity>();
            List<CookingCenterEntity> cookingCenterEntities = cookingCenterRepository.findByDomainNameAndArea(domain, criteriaValue);
            for(CookingCenterEntity cookingCenterEntity: cookingCenterEntities){
                List<SchoolEntity> schoolEntities = schoolRepository.findByDomainNameAndCookingCenterId(domain, cookingCenterEntity.getId());
                for(SchoolEntity s : schoolEntities){
                    schools.add(s);
                }
            }

            return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Wrong criteria value", HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/domain/schools", method = RequestMethod.GET)
    public ResponseEntity<?> getSchools(HttpServletRequest request, @RequestParam String domain)  {

        List<SchoolEntity> schools = schoolRepository.findByDomainName(domain);

        return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
    }

    @RequestMapping(value = "/domain/users", method = RequestMethod.GET)
    public ResponseEntity<?> getFullyRegisteredUsers(HttpServletRequest request, @RequestParam String domain)  {

        List<UserEntity> users = userRepository.findByDomainNameAndStatus(domain, 3);

        return new ResponseEntity<Integer>(users.size(), HttpStatus.OK);
    }

    @RequestMapping(value = "/domain/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(HttpServletRequest request, @RequestBody String payload)  {

        Map<String, String> responseMap = new HashMap<String, String>();

        JSONObject jsonObject =  new JSONObject(payload);
        int postCode = (int) jsonObject.get(ResponseKeys.RegisterUser_PostCode);
        //String authToken = request.getHeader(this.tokenHeader);
        int providerId = (int) jsonObject.get(ResponseKeys.RegisterUser_ProviderID); // -1;

        int age = (int) jsonObject.get(ResponseKeys.RegisterUser_Age);
        int educationalBackground = (int) jsonObject.get(ResponseKeys.RegisterUser_Educational_Background);
        int profession = (int) jsonObject.get(ResponseKeys.RegisterUser_Profession);

        //String domain = request.getHeader("Domain");
        String domain = jsonObject.getString(ResponseKeys.RegisterUser_Domain);
        //authToken = authToken.substring(7);
       // providerId = Integer.valueOf(request.getHeader("providerId"));
        String providerUUID = jsonObject.getString(ResponseKeys.RegisterUser_ProviderUUID); //jwtTokenUtil.getProviderUUIDFromToken(authToken);
        UserEntity currentUser = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, providerId, domain);

        currentUser.setPostCode(postCode);
        currentUser.setAge(age);
        currentUser.setProfession(profession);
        currentUser.setEducation(educationalBackground);

        Integer lastID = childRepository.getLastID();

        if(lastID == null)
            lastID = 0;
        List<ChildrenEntity> childrenEntities = new ChildCollectionFacade().Deserialize(jsonObject.get(ResponseKeys.RegisterUser_Children));

        for(int i=0; i< childrenEntities.size(); i++ ) {
            lastID++;
            childrenEntities.get(i).setIdchildren(lastID);
            for(ChildrenEntity childrenEntity : childrenEntities)
            {
                List<AllergyEntity> allergyEntities = new ArrayList<AllergyEntity>();
                for (Object allergenID : childrenEntity.getAllergens())
                {
                    int id = (int) allergenID;
                    allergyEntities.add(allergenRepository.findByIdallergyAndDomainName(id, domain));
                }
                childrenEntity.setAllergyEntities(allergyEntities);
            }
        }
        currentUser.setChildrenEntities(childrenEntities);
        currentUser.setStatus(UserEntity.Status.REGISTERED.getValue());
        UserEntity updatedUserEntity = userRepository.save(currentUser);
        responseMap.put("user", updatedUserEntity.Serialize().toString());
        responseMap.put("status", String.valueOf(updatedUserEntity.getStatus()));


        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }


    @RequestMapping(value = "/domain/fullyRegister", method = RequestMethod.POST)
    public ResponseEntity<?> fullyRegisterUser(HttpServletRequest request, @RequestBody String payload) {

        Map<String, String> responseMap = new HashMap<String, String>();

        System.out.println(payload);
        String authToken = request.getHeader(this.tokenHeader);
        int providerId = -1;
        String domain = request.getHeader("Domain");
        authToken = authToken.substring(7);
        providerId = Integer.valueOf(request.getHeader("providerId"));
        String providerUUID = jwtTokenUtil.getProviderUUIDFromToken(authToken);
        UserEntity currentUser = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, providerId, domain);
        currentUser.setStatus(UserEntity.Status.FULLY_REGISTERED.getValue());

        UserEntity updatedUserEntity = userRepository.save(currentUser);
        responseMap.put("user", updatedUserEntity.Serialize().toString());
        responseMap.put("status", String.valueOf(updatedUserEntity.getStatus()));

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("social@publicfood.eu");
//        message.setTo(currentUser.getEmail());
//        message.setSubject("Test");
//        message.setText("Dear " + currentUser.getDisplayname() +"\n \n" +
//                "the public food team welcomes you!");
//        emailSender.send(message);

        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);




    }

    @RequestMapping(value = "/domain/updateUser", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody String payload)  {




        Map<String, String> responseMap = new HashMap<String, String>();

        JSONObject jsonObject =  new JSONObject(payload);
        int postCode = (int) jsonObject.get(ResponseKeys.RegisterUser_PostCode);
        //String authToken = request.getHeader(this.tokenHeader);
        int providerId = (int) jsonObject.get(ResponseKeys.RegisterUser_ProviderID); // -1;

        int age = (int) jsonObject.get(ResponseKeys.RegisterUser_Age);
        int educationalBackground = (int) jsonObject.get(ResponseKeys.RegisterUser_Educational_Background);
        int profession = (int) jsonObject.get(ResponseKeys.RegisterUser_Profession);

        //String domain = request.getHeader("Domain");
        String domain = jsonObject.getString(ResponseKeys.RegisterUser_Domain);
        //authToken = authToken.substring(7);
        // providerId = Integer.valueOf(request.getHeader("providerId"));
        String providerUUID = jsonObject.getString(ResponseKeys.RegisterUser_ProviderUUID); //jwtTokenUtil.getProviderUUIDFromToken(authToken);
        UserEntity currentUser = userRepository.findByProviderUuidAndProvideridAndDomainName(providerUUID, providerId, domain);


        List<UserHasChildrenEntity> children = userHasChildrenRepository.findByUserIdAndDomainName(currentUser.getId(), domain);

        for(UserHasChildrenEntity userHasChildrenEntity : children){
            ChildrenEntity child = childRepository.findByIdchildrenAndDomainName(userHasChildrenEntity.getChildrenIdchildren(), domain);
            List<ChildrenHasAllergyEntity> childrenHasAllergyEntities = childrenHasAllergyRepository.findByChildrenIdchildrenAndDomainName(child.getIdchildren(), domain);
            for(ChildrenHasAllergyEntity childrenHasAllergyEntity : childrenHasAllergyEntities){
                childrenHasAllergyRepository.delete(childrenHasAllergyEntity);
            }
            userHasChildrenRepository.delete(userHasChildrenEntity);
            childRepository.delete(child);
        }

        currentUser.setPostCode(postCode);
        currentUser.setAge(age);
        currentUser.setProfession(profession);
        currentUser.setEducation(educationalBackground);

        Integer lastID = childRepository.getLastID();

        if(lastID == null)
            lastID = 0;
        List<ChildrenEntity> childrenEntities = new ChildCollectionFacade().Deserialize(jsonObject.get(ResponseKeys.RegisterUser_Children));

        for(int i=0; i< childrenEntities.size(); i++ ) {
            lastID++;
            childrenEntities.get(i).setIdchildren(lastID);
            for(ChildrenEntity childrenEntity : childrenEntities)
            {
                List<AllergyEntity> allergyEntities = new ArrayList<AllergyEntity>();
                for (Object allergenID : childrenEntity.getAllergens())
                {
                    int id = (int) allergenID;
                    allergyEntities.add(allergenRepository.findByIdallergyAndDomainName(id, domain));
                }
                childrenEntity.setAllergyEntities(allergyEntities);
            }
        }
        currentUser.setChildrenEntities(childrenEntities);
        currentUser.setStatus(UserEntity.Status.REGISTERED.getValue());
        UserEntity updatedUserEntity = userRepository.save(currentUser);
        responseMap.put("user", updatedUserEntity.Serialize().toString());
        responseMap.put("status", String.valueOf(updatedUserEntity.getStatus()));


        return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
    }

    }
