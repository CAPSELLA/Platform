package gr.uoa.di.madgik.publicfood;

import gr.uoa.di.madgik.publicfood.capsella_authentication_service.User;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChartsScheduledTasks {

    @Value("${server.url}")
    private String server;

    @Scheduled(fixedRate = 12 * 60 * 60 * 1000, initialDelay = 10000)
    public void calculateTotalPercentages() throws Exception {


        System.out.println("Starting charts scheduled task ...................................................");
        String url = server + "charts2/datasetsByCity";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByCity" +body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();
        }

        url = server + "charts2/datasetsByCity";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByCity " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsByArea";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByArea " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsByArea";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByArea " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }


        url = server + "charts2/datasetsByPostCode";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
        //    System.out.println("datasetsByPostCode " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsByPostCode";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByPostCode " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsByschool";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
        //    System.out.println("datasetsByschool " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsByschool";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
       //     System.out.println("datasetsByschool" + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageByArea";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
        //    System.out.println("datasetsTopPercentageByArea " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageByArea";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
        //    System.out.println("datasetsTopPercentageByArea" + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageByPostCode";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
         //   System.out.println("datasetsTopPercentageByPostCode " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageByPostCode";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
         //   System.out.println("datasetsTopPercentageByPostCode " + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageBySchool";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "asti");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
         //   System.out.println("datasetsTopPercentageBySchool" + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        url = server + "charts2/datasetsTopPercentageBySchool";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("domain", "milan");

        entity = new HttpEntity<String>(headers);
        try {
            ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            String body = result.getBody();
         //   System.out.println("datasetsTopPercentageBySchool" + body);
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();

        }

        System.out.println("Finish charts scheduled task ...................................................");

    }
}
