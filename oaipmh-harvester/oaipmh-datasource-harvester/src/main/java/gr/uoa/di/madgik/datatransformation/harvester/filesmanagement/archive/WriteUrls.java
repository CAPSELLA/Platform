package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.Message;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListRecords;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.FilesManagerWrite;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.CustomTimes;
import gr.uoa.di.madgik.datatransformation.harvester.harvesting.FetchData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

public class WriteUrls implements FilesManagerWrite {
	
private final static Logger logger = Logger.getLogger(WriteUrls.class);
	
	public final Object LOCK_WRITE = new Object();
	
	public static WriteUrls writeUrlsInstance = null;

	protected WriteUrls() {}
	
	public static WriteUrls getWriteUrlsInstance() {
		if (writeUrlsInstance==null)
			writeUrlsInstance = new WriteUrls();
		return writeUrlsInstance;
	}
	
	public void registerUriOnFile(MessageForEveryDataProvider messageForEveryDataProvider) {
		if (!messageForEveryDataProvider.getToDelete())
			RegisteredRequests.getRegisteredRequestsInstance().setInRequestsMapping(messageForEveryDataProvider.getInfoForHarvesting().getUrl(), messageForEveryDataProvider);
		else 
			RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(messageForEveryDataProvider.getInfoForHarvesting().getUrl());
	}
	
	public void registerUriOnFile(Map<String, String> parameters) {
		if (parameters.get("harvest-new-service")!=null && parameters.get("harvest-new-service").equals("true")) {
			if (parameters.get("schema").equals("dc")) {
				Message message = new Message();
				message.setUrl(parameters.get("newUri"));
				message.setVerb(GetProperties.getPropertiesInstance().getDefaultVerb());
				
				String metadataPrefix = GetProperties.getPropertiesInstance().getDefaultMetadataPrefix();
				message.setListRecords(new ListRecords.ListRecordsBuilder(metadataPrefix).build());
				
				MessageForEveryDataProvider messageForEveryDataProvider = new MessageForEveryDataProvider();
				messageForEveryDataProvider.setInfoForHarvesting(message);
				messageForEveryDataProvider.setSchemaSupportedForUrl("dc");
				if (parameters.get("defaultTime").equals("false")) {
					String time = parameters.get("newIntervalTime");
					TimeUnit timeUnit = null;
					if (parameters.get("newTimeUnit").toUpperCase().equals("DAYS")) {
						timeUnit = TimeUnit.DAYS;
					} else if (parameters.get("newTimeUnit").toUpperCase().equals("HOURS")) {
						timeUnit = TimeUnit.HOURS;
					} else if (parameters.get("newTimeUnit").toUpperCase().equals("MINUTES")) {
						timeUnit = TimeUnit.MINUTES;
					} else timeUnit = TimeUnit.DAYS; //DEFAULT
					
					CustomTimes ct = new CustomTimes(parameters.get("newUri"), Integer.parseInt(time), timeUnit);
					gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.WriteUrls.registerUriOnFile(ct);
				}
				registerUriOnFile(messageForEveryDataProvider);
			} else { /* all */
				List<String> locations = new ArrayList<>();
				List<String> schemas=null;
				try {
					schemas = FetchData.getSupportedSchemas(parameters.get("newUri"));
					if (schemas.get(0).equals("locations")) {
						locations.addAll(schemas);
						locations.remove(0);
						schemas = FetchData.getSupportedSchemas(locations.get(0));
					}
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
				if (parameters.get("defaultTime").equals("false")) {
					String time = parameters.get("newIntervalTime");
					TimeUnit timeUnit = null;
					if (parameters.get("newTimeUnit").toUpperCase().equals("DAYS")) {
						timeUnit = TimeUnit.DAYS;
					} else if (parameters.get("newTimeUnit").toUpperCase().equals("HOURS")) {
						timeUnit = TimeUnit.HOURS;
					} else if (parameters.get("newTimeUnit").toUpperCase().equals("MINUTES")) {
						timeUnit = TimeUnit.MINUTES;
					}
					CustomTimes ct = new CustomTimes(parameters.get("newUri"), Integer.parseInt(time), timeUnit);
					gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.WriteUrls.registerUriOnFile(ct);
				}
				if (schemas!=null) {
					
					List<MessageForEveryDataProvider> messages = RegisteredRequests.getRegisteredRequestsInstance().getFromRegisteredRequestsMapping(parameters.get("newUri"));
					if (messages==null) messages = new ArrayList<>();
					
					for (String schema: schemas) {
						
						Message message = new Message();
						message.setUrl(parameters.get("newUri"));
						message.setVerb(GetProperties.getPropertiesInstance().getDefaultVerb());
						
						message.setListRecords(new ListRecords.ListRecordsBuilder(schema).build());
						
						MessageForEveryDataProvider messageForEveryDataProvider = new MessageForEveryDataProvider();
						messageForEveryDataProvider.setInfoForHarvesting(message);
						
						if (!locations.isEmpty())
							messageForEveryDataProvider.setLocations(locations);
						
						messages.add(messageForEveryDataProvider);
					}
					
					for (MessageForEveryDataProvider m: messages) {
						m.setSchemaSupportedForUrl("all");
						registerUriOnFile(m);
					}
				}
			}
		} else {
			Message message = new Message();
			message.setUrl(parameters.get("uri"));
			
			if (parameters.get("verb")==null)
				message.setVerb(GetProperties.getPropertiesInstance().getDefaultVerb());
			String metadataPrefix;
			if ((metadataPrefix = parameters.get("metadataPrefix"))==null)
				metadataPrefix = GetProperties.getPropertiesInstance().getDefaultMetadataPrefix();
			
			if (parameters.get("set")!=null)
				message.setListRecords(new ListRecords.ListRecordsBuilder(metadataPrefix).set(parameters.get("set")).build());
			else message.setListRecords(new ListRecords.ListRecordsBuilder(metadataPrefix).build());
			
			
			MessageForEveryDataProvider messageForEveryDataProvider = new MessageForEveryDataProvider();
			messageForEveryDataProvider.setInfoForHarvesting(message);
			messageForEveryDataProvider.setLastHarvestingTime(null);
			messageForEveryDataProvider.setStatus(null);
			
			registerUriOnFile(messageForEveryDataProvider);
		}
	}
	
	public void writeToFile() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<MessageForEveryDataProvider> messages = RegisteredRequests.getRegisteredRequestsInstance().getAllMessages();
			objectMapper.writeValue(new File(GetProperties.getPropertiesInstance().getArchivesFile()), messages);
		} catch (JsonGenerationException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
