package gr.uoa.di.madgik.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.GeoserverTemplate;
import gr.uoa.di.madgik.model.User;

@Service
public class AuthorizationService {
	
	private GeoserverTemplate geoserverTemplate;
	
	
	@Autowired
    public void setGeoserverTemplate(AppConfig appConfig) {	
        this.geoserverTemplate = appConfig.getProperties();
    }
	
	@Autowired
	public AuthorizationService(){
		
	}
	
	public String addRuleToLayer(){
		
		return null;
	}

	public ResponseEntity<?> addUserToGeoserver( String userName, String password){
			
			String plainCreds = geoserverTemplate.getUsername()+":"+geoserverTemplate.getPassword();
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
	
			String base64Creds = new String(base64CredsBytes);
			System.out.println("autho:"+base64Creds);
			// create request body
			User user = new User(userName,password,true);
	
			// set headers
			RestTemplate restTemplate = new RestTemplate();
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getAddUserURL()).build();
	
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", "Basic " + base64Creds);
			HttpEntity<User> entity = new HttpEntity<User>(user, headers);
			System.out.println(":1"+entity.getBody().toString());
			try
			{
				ResponseEntity<String> response = restTemplate.postForEntity(geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getAddUserURL(), entity, String.class);
				return response;
			}
			catch (HttpStatusCodeException exception) {
				
				return new ResponseEntity<String>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
	
}
