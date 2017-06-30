package gr.uoa.di.madgik.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.opendata.jackan.model.CkanDataset;
import gr.uoa.di.madgik.ckan.CkanManager;
import gr.uoa.di.madgik.ckan.RecordManager;
import gr.uoa.di.madgik.controller.CKANController;
import gr.uoa.di.madgik.model.DatasetStatus;
import gr.uoa.di.madgik.model.Metadata;

@Service
public class OrchestratorService {
	
	@Value("${orchestrator.url}")
	private String url;
	@Value("${orchestrator.datasets}")
	private String datasets;
	
	@Value("${ckan.url}")
	private String ckanUrl;
	@Value("${ckan.apiKey}")
	private String apiKey;
	@Value("${ckan.organization}")
	private String organization;
	@Value("${ckan.url.purge}")
	private String purge;
	
	private final RestTemplate restTemplate;

	    public OrchestratorService(RestTemplateBuilder restTemplateBuilder) {
	        this.restTemplate = restTemplateBuilder.build();
	    }

	    public String findDatasetsForUpdate(String token, Timestamp date) throws InterruptedException {
	    	
	    	ObjectMapper objectMapper = new ObjectMapper();

	    	HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			
			MultiValueMap<String, Timestamp> params = new LinkedMultiValueMap<String, Timestamp>();
			params.add("date",date);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + datasets)
			        .queryParam("date", date);
			
			HttpEntity<String> entity = new HttpEntity<String>(headers);
	        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

	      //2. Convert JSON to List of Person objects
	    	//Define Custom Type reference for List<Person> type
	    	TypeReference<List<Metadata>> mapType = new TypeReference<List<Metadata>>() {};
	    	List<Metadata> metadata;
			try {
				metadata = objectMapper.readValue(result.getBody(), mapType);
				storeDataset(metadata);

			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        
	        return result.getBody();
	    }
	    
	    
	    private void storeDataset(List<Metadata> metadata) {
			
			System.out.println(metadata.size());
			CkanManager ckanManager = new CkanManager(ckanUrl, apiKey, organization);
			for(Metadata m : metadata)
			{
				CkanDataset dataset = RecordManager.storeRecords(m);
				
				if(ckanManager.containsDataset(dataset.getId()) && m.getStatus().equals(DatasetStatus.ACTIVE.toString()))
					ckanManager.updateDataset(dataset);
				else if (!ckanManager.containsDataset(dataset.getId()) && m.getStatus().equals(DatasetStatus.ACTIVE.toString()))
					ckanManager.addDataset(dataset);
				else if(ckanManager.containsDataset(dataset.getId()) && m.getStatus().equals(DatasetStatus.INACTIVE.toString()))
				{
					ckanManager.deleteDataset(dataset.getId());
					purge();
				}
			}
						
			
		}
	    
	    private void purge()
	    {

	    	HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", apiKey);    
	        String url = ckanUrl + purge;
	        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("purge-packages", purge);
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			try
			{
				String response = restTemplate.postForObject(uriComponents.toUri(), entity, String.class);
			}
			catch (HttpStatusCodeException exception) {
				
			}

	    }

}
