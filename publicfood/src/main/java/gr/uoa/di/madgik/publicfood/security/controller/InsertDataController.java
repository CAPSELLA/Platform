package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.security.repository.CookingCenterRepository;
import gr.uoa.di.madgik.publicfood.security.repository.PostCodeRepository;
import gr.uoa.di.madgik.publicfood.security.repository.SchoolHasFullMenuRepository;
import gr.uoa.di.madgik.publicfood.security.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InsertDataController {

    @Autowired
    private SchoolHasFullMenuRepository schoolHasFullMenuRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private PostCodeRepository postCodeRepository;
    @Autowired
    private CookingCenterRepository cookingCenterRepository;

    @RequestMapping(value = "/insert/schoolFullMenu", method = RequestMethod.POST)
    public ResponseEntity<?> insertSchoolHasFullMenu(HttpServletRequest request, @RequestParam String domain, @RequestParam int fullMenuId) {

        List<SchoolEntity> byDomainName = schoolRepository.findByDomainName(domain);
        for(SchoolEntity s : byDomainName)
        {
            SchoolHasFullMenuEntity schoolHasFullMenuEntity = new SchoolHasFullMenuEntity();
            schoolHasFullMenuEntity.setFullMenuDomain(domain);
            schoolHasFullMenuEntity.setSchoolDomainName(domain);
            schoolHasFullMenuEntity.setSchoolId(s.getId());
            schoolHasFullMenuEntity.setFullMenuId(fullMenuId);
            schoolHasFullMenuRepository.save(schoolHasFullMenuEntity);
        }
        return new ResponseEntity<String>("OK", HttpStatus.OK);

    }

    @RequestMapping(value = "/insert/postCode", method = RequestMethod.POST)
    public ResponseEntity<?> insertPostCode(HttpServletRequest request, @RequestParam String domain, @RequestParam int postCode) {

        PostCodeEntity postCodeEntity = new PostCodeEntity();
        postCodeEntity.setDomainName(domain);
        postCodeEntity.setIdPostCode(postCode);

        PostCodeEntity save = postCodeRepository.save(postCodeEntity);

        return new ResponseEntity<PostCodeEntity>(save, HttpStatus.OK);

    }


    @RequestMapping(value = "/insert/CookingCenter", method = RequestMethod.POST)
    public ResponseEntity<?> insertCoockingCenter(HttpServletRequest request, @RequestParam String domain, @RequestParam String name , @RequestParam String address
    ,@RequestParam String telephone, @RequestParam Double latitude, @RequestParam String longitude, int postCode, int area) {


        CookingCenterEntity cookingCenterEntity = new CookingCenterEntity();
        cookingCenterEntity.setDomainName(domain);
        cookingCenterEntity.setName(name);
        cookingCenterEntity.setAddress(address);
        cookingCenterEntity.setLatidude(latitude);
        cookingCenterEntity.setLongitude(longitude);
        cookingCenterEntity.setPostCodeId(postCode);
        cookingCenterEntity.setArea(area);

        CookingCenterEntity save = cookingCenterRepository.save(cookingCenterEntity);

        return new ResponseEntity<CookingCenterEntity>(save, HttpStatus.OK);

    }


    @RequestMapping(value = "/insert/School", method = RequestMethod.POST)
    public ResponseEntity<?> insertSchool(HttpServletRequest request, @RequestParam String domain, @RequestParam String name , @RequestParam String address
            , @RequestParam Double latitude, @RequestParam int cookingCenterId, @RequestParam Double longitude, int postCode) {


        SchoolEntity schoolEntity = new SchoolEntity();
        schoolEntity.setDomainName(domain);
        schoolEntity.setName(name);
        schoolEntity.setAddress(address);
        //schoolEntity.setTelephone(telephone);
        schoolEntity.setCookingCenterId(cookingCenterId);
        schoolEntity.setLatidude(latitude);
        schoolEntity.setLongitude(longitude);
        schoolEntity.setPostCodeIdPostCode(postCode);

        SchoolEntity save = schoolRepository.save(schoolEntity);

        return new ResponseEntity<SchoolEntity>(save, HttpStatus.OK);

    }

    @RequestMapping(value = "/insert/getCookingCenters", method = RequestMethod.GET)
    public ResponseEntity<?> getCookingCenters(HttpServletRequest request, @RequestParam String domain) {


        List<CookingCenterEntity> byDomainName = cookingCenterRepository.findByDomainName(domain);

        return new ResponseEntity<List<CookingCenterEntity>>(byDomainName, HttpStatus.OK);

    }

    @RequestMapping(value = "/insert/getPostcodes", method = RequestMethod.GET)
    public ResponseEntity<?> getPostcodes(HttpServletRequest request, @RequestParam String domain) {


        List<PostCodeEntity> byDomainName = postCodeRepository.findByDomainName(domain);

        return new ResponseEntity<List<PostCodeEntity>>(byDomainName, HttpStatus.OK);

    }

    @RequestMapping(value = "/insert/getSchools", method = RequestMethod.GET)
    public ResponseEntity<?> getSchools(HttpServletRequest request, @RequestParam String domain) {


        List<SchoolEntity> byDomainName = schoolRepository.findByDomainName(domain);

        return new ResponseEntity<List<SchoolEntity>>(byDomainName, HttpStatus.OK);

    }


}
