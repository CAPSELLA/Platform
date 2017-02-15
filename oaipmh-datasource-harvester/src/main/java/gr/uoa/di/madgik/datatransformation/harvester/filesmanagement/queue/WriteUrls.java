package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue;

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
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.QueuedRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.CustomTimes;
import gr.uoa.di.madgik.datatransformation.harvester.harvesting.FetchData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

public class WriteUrls implements FilesManagerWrite {
	
	private final static Logger logger = Logger.getLogger(WriteUrls.class);
	
	public void registerUriOnFile(MessageForEveryDataProvider messageForEveryDataProvider) {
		synchronized (QueuedRequests.class) {
			RWActions.readFromFile(new ReadUrls(), false);
			List<MessageForEveryDataProvider> list;
			if ((list=QueuedRequests.getQueuedRequestsInstance().getQueuedRequestsMapping().get(messageForEveryDataProvider.getInfoForHarvesting().getUrl()))==null)
				list = new ArrayList<>();
			MessageForEveryDataProvider toUpdate = null;
			if(!list.isEmpty()) {
				for (MessageForEveryDataProvider m: list) {
					if (m.getInfoForHarvesting().getListRecords().getMetadataPrefix().equals(messageForEveryDataProvider.getInfoForHarvesting().getListRecords().getMetadataPrefix())) {
						toUpdate = m;
						break;
					}
				}
			}
			if (toUpdate!=null) {
				list.remove(toUpdate);
			}
			list.add(messageForEveryDataProvider);
			QueuedRequests.getQueuedRequestsInstance().getQueuedRequestsMapping().put(messageForEveryDataProvider.getInfoForHarvesting().getUrl(), list);
			writeToFile();
			RWActions.readFromFile(new ReadUrls(), false);
		}
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
				List<String> schemas = null;
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
				if (schemas!=null)
					for (String schema: schemas) {
					
						Message message = new Message();
						message.setUrl(parameters.get("newUri"));
						message.setVerb(GetProperties.getPropertiesInstance().getDefaultVerb());
						
						message.setListRecords(new ListRecords.ListRecordsBuilder(schema).build());
						
						MessageForEveryDataProvider messageForEveryDataProvider = new MessageForEveryDataProvider();
						messageForEveryDataProvider.setInfoForHarvesting(message);
						
						if (!locations.isEmpty())
							messageForEveryDataProvider.setLocations(locations);
						
						registerUriOnFile(messageForEveryDataProvider);
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
			registerUriOnFile(messageForEveryDataProvider);
		}
	}

	
	public void writeToFile() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<MessageForEveryDataProvider> messagesForEveryDataProviders = new ArrayList<>();
			for (Map.Entry<String, List<MessageForEveryDataProvider>> entry: QueuedRequests.getQueuedRequestsInstance().getQueuedRequestsMapping().entrySet())
				for(MessageForEveryDataProvider m: entry.getValue())
					messagesForEveryDataProviders.add(m);
			objectMapper.writeValue(new File(GetProperties.getPropertiesInstance().getQueuedFile()), messagesForEveryDataProviders);
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