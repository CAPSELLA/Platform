package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.Utilities.BMICalculation;
import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.security.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Transactional
public class ChildController {

    @Autowired
    private ChildrenHasAllergyRepository childrenHasAllergyRepository;
    @Autowired
    private AllergyRepository allergyRepository;
    @Autowired
    private TranslationAllergyRepository translationAllergyRepository;
//    @Autowired
//    SurveyRepository surveyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserHasChildrenRepository userHasChildrenRepository;
    @Autowired
    ChildRepository childRepository;
    @Autowired
    SchoolRepository schoolRepository;


    @RequestMapping(value = "/kid/allergens", method = RequestMethod.GET)
    public ResponseEntity<?> getChildAllergens(HttpServletRequest request, @RequestParam int childId, @RequestParam String domain, @RequestParam String language) {

        List<ChildrenHasAllergyEntity> childrenHasAllergens = childrenHasAllergyRepository.findByChildrenIdchildrenAndDomainName(childId, domain);

        List<TranslationAllergyEntity> childAllergens = new ArrayList<TranslationAllergyEntity>();
        for (ChildrenHasAllergyEntity childrenHasAllergyEntity : childrenHasAllergens) {
            TranslationAllergyEntity allergen = translationAllergyRepository.findByAllergyDomainNameAndLanguageAndAllergyIdallergy(domain, language, childrenHasAllergyEntity.getAllergyIdallergy());
            childAllergens.add(allergen);
        }
        return new ResponseEntity<List<TranslationAllergyEntity>>(childAllergens, HttpStatus.OK);
    }

    @RequestMapping(value = "/kid/entity", method = RequestMethod.GET)
    public ResponseEntity<?> getChildAllergens(HttpServletRequest request, @RequestParam int childId, @RequestParam String domain) {

        ChildrenEntity child = childRepository.findByIdchildrenAndDomainName(childId, domain);

        return new ResponseEntity<ChildrenEntity>(child, HttpStatus.OK);

    }

    @RequestMapping(value = "/kid/bmi", method = RequestMethod.GET)
    public ResponseEntity<?> getChildBMI(HttpServletRequest request, @RequestParam int childId, @RequestParam String domain) {

        ChildrenEntity child = childRepository.findByIdchildrenAndDomainName(childId, domain);
        Map<String, String> result = BMICalculation.calculateBMI(child);

        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);

    }

    @RequestMapping(value = "/kid/edit", method = RequestMethod.POST)
    public ResponseEntity<?> editChild(HttpServletRequest request, @RequestBody String payload) {

        JSONObject jsonObject =  new JSONObject(payload);

        List<UserEntity> users = new ArrayList<UserEntity>();

        ChildrenEntity childrenEntity = new ChildrenEntity().Deserialize(jsonObject);
        UserHasChildrenEntity userHasChild = userHasChildrenRepository.findByChildrenIdchildrenAndDomainName(childrenEntity.getIdchildren(), childrenEntity.getDomainName());

        UserEntity user = userRepository.findByIdAndDomainName(userHasChild.getUserId(), userHasChild.getDomainName());

        users.add(user);
        childrenEntity.setUserEntities(users);

        List<AllergyEntity> allergyEntities = new ArrayList<AllergyEntity>();
        for (Object allergenID : childrenEntity.getAllergens())
        {
            int id = (int) allergenID;
            allergyEntities.add(allergyRepository.findByIdallergyAndDomainName(id, childrenEntity.getDomainName()));
        }
        childrenEntity.setAllergyEntities(allergyEntities);


//        List<ChildrenHasAllergyEntity> childrenHasAllergyEntities = childrenHasAllergyRepository.findByChildrenIdchildrenAndDomainName(childrenEntity.getIdchildren(), childrenEntity.getDomainName());
//        for(ChildrenHasAllergyEntity childrenHasAllergyEntity : childrenHasAllergyEntities){
//            childrenHasAllergyRepository.delete(childrenHasAllergyEntity);
//        }

        ChildrenEntity save = childRepository.save(childrenEntity);

        return new ResponseEntity<ChildrenEntity>(save, HttpStatus.OK);

    }


}