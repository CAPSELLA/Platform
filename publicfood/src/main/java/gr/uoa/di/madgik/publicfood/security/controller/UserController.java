package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.model.ChildrenEntity;
import gr.uoa.di.madgik.publicfood.model.ChildrenHasAllergyEntity;
import gr.uoa.di.madgik.publicfood.model.UserHasChildrenEntity;
import gr.uoa.di.madgik.publicfood.security.repository.ChildRepository;
import gr.uoa.di.madgik.publicfood.security.repository.ChildrenHasAllergyRepository;
import gr.uoa.di.madgik.publicfood.security.repository.UserHasChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserHasChildrenRepository userHasChildrenRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    ChildrenHasAllergyRepository childrenHasAllergyRepository;

    @RequestMapping(value = "/user/getChildren", method = RequestMethod.GET)
    public ResponseEntity<?> getChildren(HttpServletRequest request, @RequestParam int userId, @RequestParam String domain) {

        List<UserHasChildrenEntity> userChildrenIds = userHasChildrenRepository.findByUserIdAndDomainName(userId, domain);
        List<ChildrenEntity> childrenEntities = new ArrayList<ChildrenEntity>();

        for (UserHasChildrenEntity userHasChildrenEntity : userChildrenIds) {
            ChildrenEntity child = childRepository.findByIdchildrenAndDomainName(userHasChildrenEntity.getChildrenIdchildren(), domain);
//            List<ChildrenHasAllergyEntity> childAllergens = childrenHasAllergyRepository.findByChildrenIdchildrenAndDomainName(child.getIdchildren(), domain);
//            child.setAllergyEntities(childAllergens);
            childrenEntities.add(child);
        }

        return new ResponseEntity<List<ChildrenEntity>>(childrenEntities, HttpStatus.OK);

    }
}
