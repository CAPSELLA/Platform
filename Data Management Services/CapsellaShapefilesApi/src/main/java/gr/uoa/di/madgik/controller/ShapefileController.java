package gr.uoa.di.madgik.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.GeoserverTemplate;
import gr.uoa.di.madgik.model.Access;
import gr.uoa.di.madgik.model.Rule;
import gr.uoa.di.madgik.model.User;
import gr.uoa.di.madgik.service.GsService;

import java.util.Base64;

@RestController
@Component
public class ShapefileController {
	
	
	private static Logger logger = LoggerFactory.getLogger(ShapefileController.class);
	private GsService gsService;
	private GeoserverTemplate geoserverTemplate;
	
	@Value("${temp.directory}")
	private String tempPath;
	
	
	@Autowired
    public void setGeoserverTemplate(AppConfig appConfig) {	
        this.geoserverTemplate = appConfig.getProperties();
    }

	@Autowired
    public ShapefileController(GsService gsService) {	
        this.gsService = gsService;
    }
	
	@RequestMapping("/ping")
	public String ping() {
		return "pong";		
	}
	
	@RequestMapping(value = "/shapefiles", method = RequestMethod.POST)
	public ResponseEntity<?> uploadShapefile(@RequestParam("shapefile") MultipartFile uploadfile, @RequestParam("access") String access,@RequestParam("username") String username){
		
		Map<String, String> responseMap = new HashMap<String, String>();
		
		String path = tempPath;
		File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
		try {
			uploadfile.transferTo(convFile);

		ZipFile zipFile = new ZipFile(convFile);
		String datasetName="";
		
		 Enumeration zipEntries = zipFile.entries();
		    while (zipEntries.hasMoreElements()) {
		        String fileName = ((ZipEntry) zipEntries.nextElement()).getName();
		        if(FilenameUtils.getExtension(fileName).equals("shp")){
		        	Path p = Paths.get(fileName);
		        	String file = p.getFileName().toString();
	    	   		datasetName =  FilenameUtils.removeExtension(file);
	    	   			break;
	    	   		 }		 
		        }
			System.out.println("Dataset:" + datasetName);
						
				if( gsService.uploadShape(datasetName,convFile)){
					
					convFile.delete();
					if(Access.getValue(access).equals(Access.PUBLIC)){
						responseMap.put("wms-service", geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getWmsUrl());
						responseMap.put("wfs-service", geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getWfsUrl());
	
						return  new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
					}
					else if(Access.getValue(access).equals(Access.PRIVATE)){
						
						
						String plainCreds = geoserverTemplate.getUsername()+":"+geoserverTemplate.getPassword();
						byte[] plainCredsBytes = plainCreds.getBytes();
						byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);

						String base64Creds = new String(base64CredsBytes);
						System.out.println("autho:"+base64Creds);
						// create request body
						Rule rule = new Rule("1","DENY");

						// set headers
						RestTemplate restTemplate = new RestTemplate();
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_JSON);
						headers.add("Authorization", "Basic " + base64Creds);
						HttpEntity<Rule> entity = new HttpEntity<Rule>(rule, headers);
						System.out.println(":1"+entity.getBody().toString());

						try
						{
							ResponseEntity<String> response = restTemplate.postForEntity(geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getAddRuleURL(), entity, String.class);
							
						}catch (HttpStatusCodeException exception) {
							
							return new ResponseEntity<String>("first rule problem", HttpStatus.INTERNAL_SERVER_ERROR);
						}
						
						rule = new Rule("0","ALLOW","ADMIN" );

						// set headers
						
						entity = new HttpEntity<Rule>(rule, headers);
						System.out.println(":1"+entity.getBody().toString());

						try
						{
							ResponseEntity<String> response = restTemplate.postForEntity(geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getAddRuleURL(), entity, String.class);
						}catch (HttpStatusCodeException exception) {
							
							return new ResponseEntity<String>("Second rule problem", HttpStatus.INTERNAL_SERVER_ERROR);
						}
						
						rule = new Rule(username, datasetName, geoserverTemplate.getWorkspace(), "*", "*", "ALLOW", "0", "*" );

						// set headers
						
						entity = new HttpEntity<Rule>(rule, headers);
						System.out.println(":1"+entity.getBody().toString());

						try
						{
							ResponseEntity<String> response = restTemplate.postForEntity(geoserverTemplate.getGsBaseUrl()+geoserverTemplate.getAddRuleURL(), entity, String.class);
							return response;
						}
						catch (HttpStatusCodeException exception) {
							
							return new ResponseEntity<String>("Exception", HttpStatus.INTERNAL_SERVER_ERROR);
						}

					}
				}
				
				else{
				//	System.out.println(gsService.uploadShape( geoserverTemplate.getWorkspace(), geoserverTemplate.getDsName(), convFile.toURI().toURL(), datasetName));
						convFile.delete();
					 new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		
		
			
			
				
				
		} catch (IOException e) {
			
			e.printStackTrace();
			return  new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	
	}
	
	@RequestMapping(value = "/shapefiles/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteShapefile(@RequestParam("access") String access, @RequestParam("dataset_name") String layerName){
		if(Access.getValue(access).equals(Access.PUBLIC)){
			if(gsService.deleteShapefile(layerName)){
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else{
				logger.error("Resource for delete not found");
				return new ResponseEntity<String>(HttpStatus.GONE);
			}
			
		}
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> addUser( @RequestParam("username") String userName, @RequestParam("password")String password){
		
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
