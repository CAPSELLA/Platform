package gr.uoa.di.madgik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gr.uoa.di.madgik.model.Access;
import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.service.MetadataService;
import io.swagger.annotations.Api;

@Api
@RestController
@Component
public class DatasetsController {
	
	
	private MetadataService metadataService;
	private GlobalController globalController;

	@Autowired
	public void setMetadataService(MetadataService metadataService, GlobalController globalController) {
		this.metadataService = metadataService;
		this.globalController = globalController;
	}

	
	@RequestMapping(value = "/datasets/getUserDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getUserDatasets(@RequestHeader("Authorization") String token) {
		
		
		ResponseEntity<?> res = globalController.authorizeByUsername(token);
		String username = (String) res.getBody();
		
		if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
		{
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
		}
		List<Metadata> datasets = metadataService.findByUsername(username);
		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
	@RequestMapping(value = "/datasets/getGroupDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getGroupDatasets( @RequestHeader("Authorization") String token, @RequestHeader("group") String group) {
		
	ResponseEntity<?> res = globalController.authorizeByUsername(token);
	
	if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
	{
		return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
	}
		
		List<Metadata> datasets = metadataService.findByGroup(group);
		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
	
	@RequestMapping(value = "/datasets/getDataset", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Metadata> getDataset( @RequestHeader("Authorization") String token, @RequestHeader("group") String group, @RequestParam("uuid") String uuid) {
		
	ResponseEntity<?> res = globalController.authorizeByUsername(token);
	String username = (String) res.getBody();
	
	if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
	{
		return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
	}
		
		Metadata dataset = metadataService.findByUUID(uuid);
		
		if((dataset.getOwnerGroup() != null && dataset.getOwnerGroup().equals(group)) || (dataset.getUsername() != null && dataset.getUsername().equals(username)))
		{
			return new ResponseEntity<Metadata>(dataset,HttpStatus.OK);
		}
		else
			return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);

	}
	
	@RequestMapping(value = "/datasets/getPublicDatasets", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Metadata>> getPublicDatasets() {
		
		List<Metadata> datasets = metadataService.findByAccess(Access.PUBLIC.toString());
		
		return new ResponseEntity<List<Metadata>>(datasets,HttpStatus.OK);

		
	}
	
}
