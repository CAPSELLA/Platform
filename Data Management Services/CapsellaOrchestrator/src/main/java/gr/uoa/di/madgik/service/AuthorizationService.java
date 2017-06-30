package gr.uoa.di.madgik.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.ApiConfigTemplate;
import gr.uoa.di.madgik.model.Group;
import gr.uoa.di.madgik.model.Role;

import org.springframework.stereotype.Service;

import gr.uoa.di.madgik.repository.GroupRepository;
import gr.uoa.di.madgik.repository.UserTokenRepository;

@Service
public class AuthorizationService {
	
	private ApiConfigTemplate config ;
	
	@Resource
	UserTokenRepository repository;
	
	@Resource
	GroupRepository groupRepository;
	
	
	@Autowired
	public void setApiConfiTemplate(AppConfig appConfig){
		this.config = appConfig.getProperties();
	}
	
	@Autowired
	public AuthorizationService(){
		
	}
	
	public boolean isUserAuthorized (String token){
		
		if( repository.findByToken(token) != null){
			return true;
		}
		else
			return false;
			
	}
	
	public Group getGroup(String groupName){
		
		return groupRepository.findByName(groupName);
	}
	
	
	public boolean isGroupAuthorized(String groupName,Role role){
		Group group = getGroup(groupName);
		
		for (String r: group.getListRights()){
			if(Role.getValue(r).equals(role)){
				return true;
			}
		}
		
		return false;
	}
	
	
	public List<String> getUserGroups(String username, String token)
	{
		List<String> groups = new ArrayList<String>();
		
		String url = config.getAuthorizationServer() + config.getAuthorizationGetUserGroups();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try
		{
			String[] result = restTemplate.postForObject(uriComponents.toUri(), entity, String[].class);
			groups = new ArrayList<String>(Arrays.asList(result));
		}
		catch (HttpStatusCodeException exception) {
			
			return null;
		}
			
		
		
		return groups;
	}
	
	
	public boolean hasGroupAccess(String token, String group)
	{
		
		List<String> groups = new ArrayList<String>();
		
		String url = config.getAuthorizationServer() + config.getAuthorizationGetUserGroups();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	//params.add("username", username);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try
		{
			String[] result = restTemplate.postForObject(uriComponents.toUri(), entity, String[].class);
			groups = new ArrayList<String>(Arrays.asList(result));
		}
		catch (HttpStatusCodeException exception) {
			
			return false;
		}
			
		if(isGroupAuthorized(group, Role.WRITE)){
				if( groups.contains(group)){
					return true;
				}
		}

		
		return false;
	}

}
