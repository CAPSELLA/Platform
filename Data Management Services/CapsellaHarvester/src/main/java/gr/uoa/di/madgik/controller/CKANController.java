package gr.uoa.di.madgik.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.opendata.jackan.model.CkanDataset;
import gr.uoa.di.madgik.ckan.CkanManager;
import gr.uoa.di.madgik.model.Metadata;
import io.swagger.annotations.Api;

@Api
@RestController
@Component
public class CKANController {
	
	
	@Value("${ckan.url}")
	private String url;
	@Value("${ckan.apiKey}")
	private String apiKey;
	@Value("${ckan.organization}")
	private String organization;
	

	@ResponseBody
	public ResponseEntity<?> storeDataset(@RequestParam("metadata") List<Metadata> metadata) {
				
		
		CkanManager ckanManager = new CkanManager(url, apiKey, organization);
		for(Metadata m : metadata)
		{
			CkanDataset dataset = new CkanDataset();
			dataset.setId(m.getUuid());
			dataset.setName(m.getDatasetName());
			dataset.setOwnerOrg(m.getOwnerGroup());
			if(m.getAccess().equals("public"))
			{
				dataset.setPriv(false);
				dataset.setOpen(true);
			}
			else
			{
				dataset.setPriv(true);
				dataset.setOpen(false);
			}
		//	dataset.setUrl(m.getEndpoint());
			dataset.setRevisionTimestamp(m.getLastUpdated());
			dataset.setType(m.getContentType());
			dataset.setAuthor(m.getAuthor());
			if(ckanManager.containsDataset(dataset.getId()))
				ckanManager.updateDataset(dataset);
			else
				ckanManager.addDataset(dataset);
		}
		
		
		
		return new ResponseEntity<String>(HttpStatus.OK);
		
		
	}


}
