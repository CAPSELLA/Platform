package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.FilesManagerRead;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

public class ReadUrls implements FilesManagerRead {

	private final static Logger logger = Logger.getLogger(ReadUrls.class);

	public void readFromFile(boolean initialization) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
		
			File file = new File(GetProperties.getPropertiesInstance().getArchivesFile());
			
			if (file != null && file.canRead() && file.length()!=0) {
				List<MessageForEveryDataProvider> messages;
				messages = Arrays.asList(objectMapper.readValue(file, MessageForEveryDataProvider[].class));
				RegisteredRequests.getRegisteredRequestsInstance().getRegisteredRequestsMapping().clear();
				for (MessageForEveryDataProvider message: messages) {
					if (initialization && message.isExecuting())
						message.setExecuting(false);
					RegisteredRequests.getRegisteredRequestsInstance().setInRequestsMapping(message.getInfoForHarvesting().getUrl(), message);
				}
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			
		}
	}
	
}
