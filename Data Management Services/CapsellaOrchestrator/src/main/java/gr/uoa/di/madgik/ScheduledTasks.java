package gr.uoa.di.madgik;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.ApiConfigTemplate;
import gr.uoa.di.madgik.controller.DatasetsController;
import gr.uoa.di.madgik.model.ContentType;
import gr.uoa.di.madgik.model.DatasetStatus;
import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.service.MetadataService;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private ApiConfigTemplate config ;

    private final MetadataService metadataService;
    
    public static Timestamp date = new Timestamp(1431822000);

    public ScheduledTasks(MetadataService metadataService) {
        this.metadataService = metadataService;
    }
    
    @Autowired
	public void setApiConfiTemplate(AppConfig appConfig){
		this.config = appConfig.getProperties();
	}


    @Scheduled(fixedRate = 1000 * 60 * 1)
    public void reportCurrentTime() {
    	
    	System.out.println("Orchestrator check for delete ... " + date);
    	try {
			List<Metadata> metadata =  metadataService.findDatasetsFordelete(date);
			System.out.println("Metadata size: " + metadata.size());
			for (Metadata m: metadata)
			{
				System.out.println("Status: " + m.getStatus());
				
				
				if(ContentType.getValue(m.getContentType()).equals(ContentType.CSV))
				{
					
					MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
					postParams.add("uuid", m.getUuid().toString());
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getCsvServer() + config.getCsvServer_deleteCSVFile(),
							postParams, String.class);
					
					
	
					
				}
				else if(ContentType.getValue(m.getContentType()).equals(ContentType.JSON))
				{
					
					MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
					postParams.add("uuid", m.getUuid().toString());
					postParams.add("collection", m.getOwnerGroup());
					
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getMongoDBServer() + config.getMongoDBServer_deleteJSONFile(),
							postParams, String.class);
				
					
				}
				else if(ContentType.getValue(m.getContentType()).equals(ContentType.SHAPEFILE))
				{
					
					MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
					
					
					if (m.getAccess() != null)
						postParams.add("access", m.getAccess());
					else
						postParams.add("access", "public");
					
					postParams.add("dataset_name", m.getDatasetName());
				
					
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<String> response = restTemplate.postForEntity(
							config.getShapeServer() + config.getShapeDelete(),
							postParams, String.class);
				
					
				}
				else if(ContentType.getValue(m.getContentType()).equals(ContentType.RELATIONAL))
				{
					
					MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
					postParams.add("database", m.getDatasetName());
					
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getRelationalServer() + config.getRelationalServer_deleteDatabase(),
							postParams, String.class);
				
					
				}
				else if(ContentType.getValue(m.getContentType()).equals(ContentType.IMAGE_FILE))
				{
					
					MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
					postParams.add("uuid", m.getUuid().toString());
					RestTemplate restTemplate = new RestTemplate();
					String foo = restTemplate.postForObject(
							config.getImageServer() + config.getImageServer_deleteImageFile(),
							postParams, String.class);
					
					
	
					
				}

				
				System.out.println("Delete dataset with uuid: " + m.getUuid());
				metadataService.deleteByUUID(m.getUuid());
				
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}