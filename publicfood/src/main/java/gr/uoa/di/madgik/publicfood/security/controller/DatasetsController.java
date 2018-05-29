package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.Utilities.DatasetsGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class DatasetsController {


    @RequestMapping(value = "/datasets/asti", method = RequestMethod.GET)
    public ResponseEntity<?> editChild() {

        String statisticsBySchool = DatasetsGenerator.createStatisticsBySchool();

        return new ResponseEntity<String>(statisticsBySchool, HttpStatus.OK);

    }
}
