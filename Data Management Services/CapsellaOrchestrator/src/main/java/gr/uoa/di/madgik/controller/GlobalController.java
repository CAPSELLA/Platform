package gr.uoa.di.madgik.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.ApiConfigTemplate;
import gr.uoa.di.madgik.model.ContentType;
import gr.uoa.di.madgik.model.DatasetStatus;
import gr.uoa.di.madgik.model.Endpoint;
import gr.uoa.di.madgik.model.EndpointType;
import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.model.Role;
import gr.uoa.di.madgik.repository.GroupRepository;
import gr.uoa.di.madgik.repository.MetadataRepository;
import gr.uoa.di.madgik.service.AuthorizationService;
import gr.uoa.di.madgik.service.MetadataService;
import gr.uoa.di.madgik.utils.UtilFunctions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@Component
public class GlobalController {

	private static Logger logger = LoggerFactory.getLogger(GlobalController.class);

	
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private MetadataService metadataService;
	private AuthorizationService authorizationService;
	
	@Value("${temp.directory}")
	private String tempPath;
	
	@Value("${license.default}")
	private String default_license;
	
	private ApiConfigTemplate config ;
	
	@Autowired
	private MetadataRepository metadataRepository;
	
	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Autowired
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}
	
	@Autowired
	public void setApiConfiTemplate(AppConfig appConfig){
		this.config = appConfig.getProperties();
	}
	
	@ApiImplicitParams({
		  @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
                  required = true, dataType = "string", paramType = "header"),
		  @ApiImplicitParam(name = "Group", value = "Authorization token", 
          required = true, dataType = "access_token", paramType = "header"),
		  
	    @ApiImplicitParam(name = "metadata", value = "{ \n" +
		"\"content-type\":\"csv\", \n"+
        "\"description\":\"Description of Dataset\", \n" +
        "\"access\":\"public\", \n"+
       "\"dataset_name\":\"testDatasetName\", \n"+
		"\"tags\":\"newtags\" \n"+
		"}", required = true, dataType = "string", paramType = "query"),
	   
	   
	  })
	@ApiOperation(value = "Returns file uuid and endpoint location", notes = "Returns file uuid and endpoint location", response = ResponseEntity.class)
	@RequestMapping(value = "/datasets", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> storeDatasetMetadata(@RequestParam("metadata") String metadataParam,
			 @RequestHeader("Authorization") String token, @RequestHeader("Group") String group, HttpServletRequest request) {

		
		ResponseEntity<?> response = authorizeByUsername(request, token);
		
		if(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		}
		
		
		String message = "";
		System.out.println("headers are:"+request.getAuthType());
		System.out.println("token header:"+token);
		Map<String, String> metadataMap;
		Metadata metadata = new Metadata();
		System.out.println("grou header:"+authorizationService.isGroupAuthorized(group,Role.WRITE));

		try {
			metadataMap = mapper.readValue(metadataParam, new TypeReference<Map<String, String>>() {});
			
			metadata = UtilFunctions.convertMapToMetadata(metadataMap,group);
			metadata = UtilFunctions.addDerrivedMetadata(metadata, default_license);
			if (metadata.getUsername() == null){
				metadata.setUsername(response.getBody().toString());
			}
			
			if(!authorizationService.hasGroupAccess(token, group))
				return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
			
		
			logger.info("Passed has group authorization, inserting metadata...");
			metadata.setStatus(DatasetStatus.ONUPDATE.toString());
			metadata = metadataService.insertMetadata(metadata);
 			
		} catch (JsonParseException e) {
			message = "Unsupported metadata format. Here is an example: { \"content-type\":\"csv\", \"username\":\"username\", \"access\":\"public\", \"dataset_name\":\"testcsv\", \"tags\":\"newtags\" }";
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message = "Unsupported metadata format. Here is an example: { \"content-type\":\"csv\", \"username\":\"username\", \"access\":\"public\", \"dataset_name\":\"testcsv\", \"tags\":\"newtags\" }";
			e.printStackTrace();
		} catch (IOException e) {
			message = "Unsupported metadata format. Here is an example: { \"content-type\":\"csv\", \"username\":\"username\", \"access\":\"public\", \"dataset_name\":\"testcsv\", \"tags\":\"newtags\" }";
			e.printStackTrace();
		}
		if( metadata.getUuid()!= null){
			String responseString = request.getRequestURL() +"/"+metadata.getUuid();
		URI location;
		try {
			location = new URI(responseString);
			
		} catch (URISyntaxException e) {
			location = null;
			e.printStackTrace();
		}
		Map<String, String> responseMap = new HashMap<String, String>();

		responseMap.put("location",responseString);
		
		
		return  new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
		
		 
		 
      //  return ResponseEntity.created(location).build();
		}
		else{
			return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/authorize", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> authorizeByUsername(HttpServletRequest request, @RequestHeader("Authorization") String token) {
		
		String  response;
		String url = config.getAuthorizationServer() + config.getAuthorizationByUsername();
		
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		params.add("username", username);
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).build();
		
		// Get client's IP address
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }  
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.set("Authorization", token);
		headers.set("clientIp", ipAddress);
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try
		{
			response = restTemplate.postForObject(uriComponents.toUri(), entity, String.class);
			System.out.println(response);
		}
		catch (HttpStatusCodeException exception) {
			
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		}
			
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}
	

	@ApiImplicitParams({
		  @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
                required = true, dataType = "string", paramType = "header"),
		  @ApiImplicitParam(name = "Group", value = "Authorization token", 
        required = true, dataType = "string", paramType = "header"),		

	   
	  })
	@RequestMapping(value = "/datasets/{uuid}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> storeDataset(HttpServletRequest req, @RequestParam("uploadfile") MultipartFile uploadfile,
			@PathVariable String uuid, @RequestHeader("Authorization") String token, @RequestHeader("Group") String group) {


		
		ResponseEntity<?> res = authorizeByUsername(req, token);
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		}
		
		if(!authorizationService.hasGroupAccess(token, group))
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
	
		
		Metadata metadata = metadataService.findByUUID(uuid);
		if(metadata != null)
		{
			System.out.println("The content type of the file is:"+uploadfile.getContentType());

			metadata = UtilFunctions.addContentType(metadata, uploadfile.getOriginalFilename());
			metadata = metadataService.insertMetadata(metadata);
			
		//	tempPath = System.getProperty("user.dir");		//for local testing

			if(ContentType.getValue(metadata.getContentType()).equals(ContentType.CSV))
			{
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				try {
					uploadfile.transferTo(convFile);
					multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
					multipartMap.add("uuid", uuid.toString());
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
					String csvPath = restTemplate.postForObject(
							config.getCsvServer() + config.getCsvServer_uploadCSVFile(),
							request, String.class);
					convFile.delete();
					metadataService.insertEndpoint(metadata, csvPath,EndpointType.SERVICE.toString());
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					metadata.setLastUpdated(timestamp);
					metadataService.insertMetadata(metadata);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.SHAPEFILE))
			{
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				try {
					uploadfile.transferTo(convFile);

					multipartMap.add("shapefile", new FileSystemResource(convFile.getPath()));
					if (metadata.getAccess() != null)
						multipartMap.add("access", metadata.getAccess());
					else
						multipartMap.add("access", "public");
					
					multipartMap.add("username", metadata.getUsername());
					HttpHeaders headers = new HttpHeaders();
					System.out.println("Config shape server is:"+config.getShapeServer());
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<String> response = restTemplate.postForEntity(
							config.getShapeServer(),
							request, String.class);
					System.out.println("The anwser form geoserver is:"+response);
					convFile.delete();
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					metadata.setLastUpdated(timestamp);
					metadataService.insertMetadata(metadata);
					
					if( response.getStatusCode().equals(HttpStatus.OK)){
						Map<String, String> responseMap;
						responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
						for (Map.Entry<String, String> entry : responseMap.entrySet())
						{
							if (entry.getKey().equals("wms-service")){
								metadataService.insertEndpoint(metadata, entry.getValue(),EndpointType.getValue("native").toString());
							}
							else if(entry.getKey().equals("wfs-service")){
								metadataService.insertEndpoint(metadata, entry.getValue(),EndpointType.getValue("native").toString());
							}
						}
					
					}
				
					
					return new ResponseEntity<String>(HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
					return new ResponseEntity<String>( HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.JSON))
			{
				
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				try {
					uploadfile.transferTo(convFile);
					multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
					multipartMap.add("uuid", uuid.toString());
					multipartMap.add("collection", metadata.getOwnerGroup());
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<String> response = restTemplate.postForEntity(
							config.getMongoDBServer() + config.getMongoDBServer_uploadJSONFile(),
							request, String.class);
					convFile.delete();
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					metadata.setLastUpdated(timestamp);
					metadataService.insertMetadata(metadata);
					if( response.getStatusCode().equals(HttpStatus.OK)){
						Map<String, String> responseMap;
						responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
						for (Map.Entry<String, String> entry : responseMap.entrySet())
						{
							if (entry.getKey().equals("mongo-service")){
								metadataService.insertEndpoint(metadata, entry.getValue(),EndpointType.SERVICE.toString());
							}
							
						}
					
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new ResponseEntity<String>(HttpStatus.OK);
				
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.RELATIONAL))
			{
		        RestTemplate restTemplate = new RestTemplate();
//
//
		    	HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getRelationalServer() + config.getRelationalServer_createDatabase())
				        .queryParam("database", metadata.getDatasetName());
				
				HttpEntity<String> entity = new HttpEntity<String>(headers);
		        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
				
				if(result.getStatusCode().equals(HttpStatus.OK))
				{
				
					String path = tempPath;//System.getProperty("user.dir");		//for local testing

					MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
					File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
					try {
						uploadfile.transferTo(convFile);
						multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
						multipartMap.add("database", metadata.getDatasetName());
					    headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					
					    restTemplate = new RestTemplate();
						ResponseEntity<String> response = restTemplate.postForEntity(
								config.getRelationalServer() + config.getRelationalServer_storeDatabase(),
								request, String.class);
						convFile.delete();
						metadata.setStatus(DatasetStatus.ACTIVE.toString());
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						metadata.setLastUpdated(timestamp);
						metadataService.insertMetadata(metadata);
						if( response.getStatusCode().equals(HttpStatus.OK)){
							Map<String, String> responseMap;
							responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
							for (Map.Entry<String, String> entry : responseMap.entrySet())
							{
//								if (entry.getKey().equals("mongo-service")){
//									metadataService.insertEndpoint(metadata, entry.getValue(),EndpointType.SERVICE.toString());
//								}
								
							}
						
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return new ResponseEntity<String>(HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<String>("Cannot create database!", HttpStatus.NOT_FOUND);
				}
			}
			
		}
		
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	
	
	@ApiImplicitParams({
		  @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
              required = true, dataType = "string", paramType = "header"),
		  @ApiImplicitParam(name = "Group", value = "Authorization token", 
      required = true, dataType = "string", paramType = "header"),		
	    @ApiImplicitParam(name = "uuid", value = "dataset's uuid", required = true, dataType = "string", paramType = "query"),
	   
	  })
	@RequestMapping(value = "/datasets/{uuid}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteDataset(HttpServletRequest request, @PathVariable String uuid,  @RequestHeader("Authorization") String token, @RequestHeader("Group") String group) {
		
		
		
		ResponseEntity<?> res = authorizeByUsername(request, token);
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		}
		if(!authorizationService.hasGroupAccess(token, group))
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		
		Metadata metadata = metadataService.findByUUID(uuid);
		if(metadata != null)
		{
			
				metadata.setStatus(DatasetStatus.INACTIVE.toString());
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				metadata.setLastUpdated(timestamp);
				metadataService.insertMetadata(metadata);
			
//			if(ContentType.getValue(metadata.getContentType()).equals(ContentType.CSV))
//			{
//				
//				MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
//				postParams.add("uuid", uuid.toString());
//				RestTemplate restTemplate = new RestTemplate();
//				String foo = restTemplate.postForObject(
//						config.getCsvServer() + config.getCsvServer_deleteCSVFile(),
//						postParams, String.class);
//				
//				metadataService.deleteByUUID(uuid);
//				return new ResponseEntity<String>(HttpStatus.OK);
//
//				
//			}
//			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.JSON))
//			{
//				
//				MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
//				postParams.add("uuid", uuid.toString());
//				postParams.add("collection", metadata.getOwnerGroup());
//				
//				RestTemplate restTemplate = new RestTemplate();
//				String foo = restTemplate.postForObject(
//						config.getMongoDBServer() + config.getMongoDBServer_deleteJSONFile(),
//						postParams, String.class);
//			
//				return new ResponseEntity<String>(HttpStatus.OK);
//				
//			}
//			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.SHAPEFILE))
//			{
//				
//				MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
//				
//				
//				if (metadata.getAccess() != null)
//					postParams.add("access", metadata.getAccess());
//				else
//					postParams.add("access", "public");
//				
//				postParams.add("dataset_name", metadata.getDatasetName());
//			
//				
//				RestTemplate restTemplate = new RestTemplate();
//				ResponseEntity<String> response = restTemplate.postForEntity(
//						config.getShapeServer() + config.getShapeDelete(),
//						postParams, String.class);
//			
//				return response;
//				
//			}
			
			
			
			
			
			
		}
			
		
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);


	}
	
	
	
	@ApiImplicitParams({
		  @ApiImplicitParam(name = "Authorization", value = "Authorization token", 
              required = true, dataType = "string", paramType = "header"),
		  @ApiImplicitParam(name = "Group", value = "Authorization token", 
      required = true, dataType = "string", paramType = "header"),		
	    @ApiImplicitParam(name = "uuid", value = "dataset's uuid", required = true, dataType = "string", paramType = "path"),
	   
	  })
	@RequestMapping(value = "/datasets/{uuid}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> updateDataset(HttpServletRequest req, @RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam(value="metadata", required = false) String metadataParam,
			@PathVariable String uuid,  @RequestHeader("Authorization") String token, @RequestHeader("Group") String group) {
		
		
		ResponseEntity<?> res = authorizeByUsername(req, token);
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		}
		if(!authorizationService.hasGroupAccess(token, group))
			return new ResponseEntity<String>( HttpStatus.UNAUTHORIZED);
		
		Metadata metadata = metadataService.findByUUID(uuid);
		metadata.setStatus(DatasetStatus.ONUPDATE.toString());
		metadataService.insertMetadata(metadata);
		
		Metadata newMetadata = null;
		if(metadataParam != null){
			
			newMetadata = new Metadata();
			Map<String, String> metadataMap;
			try {
				metadataMap = mapper.readValue(metadataParam, new TypeReference<Map<String, String>>() {});
			
				
				newMetadata  = UtilFunctions.convertMapToMetadata(metadataMap,group);
				newMetadata = UtilFunctions.mergeMetadata(newMetadata, metadata);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(metadata != null)
		{
			if(ContentType.getValue(metadata.getContentType()).equals(ContentType.CSV))
			{
				
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				String csvPath = null;
				try {
					uploadfile.transferTo(convFile);
					multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
					multipartMap.add("uuid", uuid.toString());
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
				    csvPath = restTemplate.postForObject(
							config.getCsvServer() + config.getCsvServer_uploadCSVFile(),
							request, String.class);
					convFile.delete();
					
					
				} catch (IOException e) {
					
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(metadata);
					metadataService.insertEndpoint(metadata, csvPath,EndpointType.SERVICE.toString());

					e.printStackTrace();
				}
				if( newMetadata != null){
					newMetadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(newMetadata);
					metadataService.insertEndpoint(newMetadata, csvPath,EndpointType.SERVICE.toString());

				}
				else{
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(metadata);
					metadataService.insertEndpoint(metadata, csvPath,EndpointType.SERVICE.toString());

				}
				return new ResponseEntity<String>(HttpStatus.OK);
				
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.JSON))
			{
				
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				try {
					uploadfile.transferTo(convFile);
					multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
					multipartMap.add("uuid", uuid.toString());
					multipartMap.add("collection", metadata.getOwnerGroup());
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getMongoDBServer() + config.getMongoDBServer_updateJSONFile(),
							request, String.class);
					convFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(metadata);
					
				}
				
				if( newMetadata != null){
					newMetadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(newMetadata);
				}
				else{
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(newMetadata);
				}
				
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.SHAPEFILE))
			{
				MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
				
				postParams.add("dataset_name", metadata.getDatasetName());
				postParams.add("access", metadata.getAccess());
				
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.postForEntity(
						config.getShapeServer() + config.getShapeDelete(),
						postParams, String.class);
				if(response.getStatusCode().equals(HttpStatus.OK)){
					
					String path = tempPath;
					MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
					File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
					try {
						uploadfile.transferTo(convFile);

						multipartMap.add("shapefile", new FileSystemResource(convFile.getPath()));
						if (metadata.getAccess() != null)
							multipartMap.add("access", metadata.getAccess());
						else
							multipartMap.add("access", "public");
						
						
						HttpHeaders headers = new HttpHeaders();
						System.out.println("Config shape server is:"+config.getShapeServer());
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
						restTemplate = new RestTemplate();
						response = restTemplate.postForEntity(
								config.getShapeServer(),
								request, String.class);
						System.out.println("The anwser form geoserver is:"+response);
						convFile.delete();
						
						if( newMetadata != null){
							newMetadata.setStatus(DatasetStatus.ACTIVE.toString());
							metadataService.insertMetadata(newMetadata);
						}
						else{
							metadata.setStatus(DatasetStatus.ACTIVE.toString());
							metadataService.insertMetadata(metadata);
						}
						return response;
					} catch (IOException e) {
						
						e.printStackTrace();
						
						metadata.setStatus(DatasetStatus.ACTIVE.toString());
						metadataService.insertMetadata(metadata);
						
						return new ResponseEntity<String>( HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
				}
				
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.JSON))
			{
				
				String path = tempPath;
				MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
				File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
				try {
					uploadfile.transferTo(convFile);
					multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
					multipartMap.add("uuid", uuid.toString());
					multipartMap.add("collection", metadata.getOwnerGroup());
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getMongoDBServer() + config.getMongoDBServer_updateJSONFile(),
							request, String.class);
					convFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(metadata);
					
				}
				
				if( newMetadata != null){
					newMetadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(newMetadata);
				}
				else{
					metadata.setStatus(DatasetStatus.ACTIVE.toString());
					metadataService.insertMetadata(newMetadata);
				}
				
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else if(ContentType.getValue(metadata.getContentType()).equals(ContentType.RELATIONAL))
			{
		        RestTemplate restTemplate = new RestTemplate();
//
//
		    	HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

			
				
					String path = tempPath; // System.getProperty("user.dir");		//for local testing

					MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
					File convFile = new File(path + File.separator + uploadfile.getOriginalFilename());
					try {
						uploadfile.transferTo(convFile);
						multipartMap.add("uploadfile", new FileSystemResource(convFile.getPath()));
						multipartMap.add("database", metadata.getDatasetName());
					    headers = new HttpHeaders();
						headers.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<Object> request = new HttpEntity<Object>(multipartMap, headers);
					
					    restTemplate = new RestTemplate();
						ResponseEntity<String> response = restTemplate.postForEntity(
								config.getRelationalServer() + config.getRelationalServer_updateDatabase(),
								request, String.class);
						convFile.delete();
						metadata.setStatus(DatasetStatus.ACTIVE.toString());
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						metadata.setLastUpdated(timestamp);
						metadataService.insertMetadata(metadata);
						if( response.getStatusCode().equals(HttpStatus.OK)){
							Map<String, String> responseMap;
							responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
							for (Map.Entry<String, String> entry : responseMap.entrySet())
							{
//								if (entry.getKey().equals("mongo-service")){
//									metadataService.insertEndpoint(metadata, entry.getValue(),EndpointType.SERVICE.toString());
//								}
								
							}
						
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return new ResponseEntity<String>(HttpStatus.OK);
				
			}
		}
	
		if( newMetadata != null){
			newMetadata.setStatus(DatasetStatus.ACTIVE.toString());
			metadataService.insertMetadata(newMetadata);
		}
		else{
			metadata.setStatus(DatasetStatus.ACTIVE.toString());
			metadataService.insertMetadata(newMetadata);
		}
		
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

	}
	
//	@RequestMapping(value = "/test", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<String> testFunction( HttpServletRequest request) {
//
//		List<String> urls = metadataService.findEndpointsByUuid("2911c001-5823-4c60-b1d5-a832a70fa2c8");
//		for(String s: urls){
//			System.out.println("the url is:"+s);
//		}
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
}
