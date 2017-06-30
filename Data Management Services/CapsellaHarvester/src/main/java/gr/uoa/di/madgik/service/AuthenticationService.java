package gr.uoa.di.madgik.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthenticationService {
	
	
	public static String authenticate(String url, String username, String password)
	{		
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", username);
		params.add("password", password);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try
		{
			String result = restTemplate.postForObject(uriComponents.toUri(), entity, String.class);
			return result;
		}
		catch (HttpStatusCodeException exception) {
			
			return null;
		}
			
		
		
		
	}

}
