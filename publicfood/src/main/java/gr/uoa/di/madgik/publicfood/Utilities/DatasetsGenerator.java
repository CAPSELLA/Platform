package gr.uoa.di.madgik.publicfood.Utilities;

import gr.uoa.di.madgik.publicfood.security.controller.ChartsController;
import org.springframework.http.ResponseEntity;

public class DatasetsGenerator {


    public static String createStatisticsBySchool(){

        ChartsController chartsController = new ChartsController();

        ResponseEntity<?> asti = chartsController.dontHaveTimeToCookVegetables(null, "milan", "1", "1");

        String body = asti.getBody().toString();

        return body;
    }
}
