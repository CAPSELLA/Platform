package gr.uoa.di.madgik.controller;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.ScheduledTasks;
import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.ApiConfigTemplate;
import gr.uoa.di.madgik.model.Access;
import gr.uoa.di.madgik.model.ContentType;
import gr.uoa.di.madgik.model.DatasetStatus;
import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.service.AuthorizationService;
import gr.uoa.di.madgik.service.MetadataService;
import io.swagger.annotations.Api;

@Api
@RestController
@Component
public class DatasetsController {
	

	private MetadataService metadataService;
	private GlobalController globalController;
	private RestTemplate restTemplate;
	private ApiConfigTemplate config ;
	private AuthorizationService authorizationService;

	public Timestamp lastHarvesterCall = new Timestamp(1431822000);

	@Autowired
	public void setMetadataService(MetadataService metadataService, GlobalController globalController, RestTemplateBuilder restTemplateBuilder) {
		this.metadataService = metadataService;
		this.globalController = globalController;
		this.restTemplate = restTemplateBuilder.build();
	}

	@Autowired
	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	
	@Autowired
	public void setApiConfiTemplate(AppConfig appConfig){
		this.config = appConfig.getProperties();
	}
	
	@RequestMapping(value = "/datasets/getUserDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getUserDatasets(HttpServletRequest request, @RequestHeader("Authorization") String token) {
		
		
		ResponseEntity<?> res = globalController.authorizeByUsername(request, token);
		String username = (String) res.getBody();
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		}
		List<Metadata> datasets = metadataService.findByUsername(username, DatasetStatus.ACTIVE.toString());
		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
	@RequestMapping(value = "/datasets/getGroupDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getGroupDatasets(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestHeader("group") String group) {
		
		ResponseEntity<?> res = globalController.authorizeByUsername(request, token);

		String username = (String) res.getBody();


		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		}
		if(!authorizationService.hasGroupReadAccess(token, group))
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		
		List<Metadata> datasets = metadataService.findByGroup(group, DatasetStatus.ACTIVE.toString());
		List<Metadata> finalDatasets = new ArrayList<>();
		if(datasets != null) {
			for (Metadata metadata : datasets) {
				if (metadata != null) {
					if (metadata.getAccess().equals(Access.PRIVATE.toString()))
					{
						if(metadata.getUsername().equals(username))
							finalDatasets.add(metadata);
					}
					else
					{
						finalDatasets.add(metadata);
					}
				}
			}
		}
		
		return new ResponseEntity<List<Metadata>>(finalDatasets,HttpStatus.OK);

		
	}
	
	
	@RequestMapping(value = "/datasets/getDataset/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDataset( HttpServletRequest request,@RequestHeader("Authorization") String token, @RequestHeader("group") String group, @PathVariable String id) {
		
		ResponseEntity<?> res = globalController.authorizeByUsername(request, token);
		String username = (String) res.getBody();
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		}

		
		Metadata dataset = metadataService.findByUUID(id);


		if(dataset != null) {

			if (dataset.getAccess().equals(Access.PRIVATE.toString()) && !dataset.getUsername().equals(username))
				return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);


			if(!dataset.getAccess().equals(Access.PUBLIC.toString())) {
				if(!authorizationService.hasGroupReadAccess(token, group))
					return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
			}
		}

		
		if( dataset != null && ((dataset.getOwnerGroup() != null && dataset.getOwnerGroup().equals(group)) || (dataset.getUsername() != null && dataset.getUsername().equals(username))))
		{
			if(dataset.getStatus().equals(DatasetStatus.ACTIVE.toString()))
			{
				
				if(dataset.getContentType().equals(ContentType.CSV.toString()))
				{

			    	HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getCsvServer() + config.getCsvServer_getCSVFile() + id);					
					HttpEntity<String> entity = new HttpEntity<String>(headers);
					System.out.println(builder.build().encode().toUri());
			        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
			        if(result.getStatusCode() == HttpStatus.OK)
			        	return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
			        
				}
				else if (dataset.getContentType().equals(ContentType.JSON.toString()))
				{
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getMongoDBServer() + config.getMongoDBServer_getJSONFile() + id).queryParam("collection", group);				
					HttpEntity<String> entity = new HttpEntity<String>(headers);
					System.out.println(builder.build().encode().toUri());
			        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
			        if(result.getStatusCode() == HttpStatus.OK)
			        	return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
					
				}
				else if(dataset.getContentType().equals(ContentType.SHAPEFILE.toString()))
				{
					
				}
				else if(dataset.getContentType().equals(ContentType.RELATIONAL.toString()))
				{
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					
					UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getRelationalServer() + config.getRelationalServer_DatabaseToJson())
					        .queryParam("database", dataset.getDatasetName());
					
					HttpEntity<String> entity = new HttpEntity<String>(headers);
			        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
			        
			        if(result.getStatusCode() == HttpStatus.OK)
			        	return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
					
				}
				else if(dataset.getContentType().equals(ContentType.IMAGE_FILE.toString()))
				{

			    	HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					String type;
					MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
					
					if(dataset.getDatasetName().contains(".jpg"))
						type = "jpg";
					else 
						type = "png";
					
					params.add("image_type", type);
					
					UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getImageServer() + config.getImageServer_getImageFile() + id).queryParams(params);					
					HttpEntity<String> entity = new HttpEntity<String>(headers);
					System.out.println(builder.build().encode().toUri());
			        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
			        
//			        byte[] bytearray = Base64.decode(result.getBody());
//			        
//					BufferedImage imag;
//					try {
//						imag = ImageIO.read(new ByteArrayInputStream(bytearray));
//						File f = new File(System.getProperty("user.dir") + File.separator + "snap."+ type);
//						ImageIO.write(imag, type, f);
//
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			
			        
			        if(result.getStatusCode() == HttpStatus.OK)
			        	return new ResponseEntity<>(result.getBody(), HttpStatus.OK);
			        
				}
				
			}
			else
				return new ResponseEntity<>( HttpStatus.NOT_FOUND);

		}
		
		return new ResponseEntity<>( HttpStatus.NOT_FOUND);

	}
	
	@RequestMapping(value = "/datasets/getPublicDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getPublicDatasets() {
		
		List<Metadata> datasets = metadataService.findByAccess(Access.PUBLIC.toString(), DatasetStatus.ACTIVE.toString());
		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
	@RequestMapping(value = "/datasets/getDatasetsForUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getDatasetsForUpdate(HttpServletRequest request,@RequestHeader("Authorization") String token, @RequestParam("date") Timestamp date) {
		
		lastHarvesterCall = date;
		ScheduledTasks.date = date;
		ResponseEntity<?> res = globalController.authorizeByUsername(request, token);
		String username = (String) res.getBody();
		
    	System.out.println("Orchestrator check for updated datasets ... " + date);

		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED) || !username.equals("admin"))
		{
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		}
		
		List<Metadata> datasets = metadataService.findByDate(date);
		
		for(Metadata metadata : datasets)
		{
			metadata.setEndpoints(metadataService.findEndpointsByUuid(metadata.getUuid()));
		}
		
    	System.out.println("Number of updated datasets ... " + datasets.size());

		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
	
	
}
