package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.FilesManagerRead;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

public class ReadUrls implements FilesManagerRead {
	
	private final static Logger logger = Logger.getLogger(ReadUrls.class);
	
	public void readFromFile(boolean initialization) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	
			File file = new File(GetProperties.getPropertiesInstance().getTimesFile());
			
			if (file != null && file.canRead() && file.length()!=0) {
				TimesFormatInJsonFile timesFormatInJsonFile = objectMapper.readValue(file, TimesFormatInJsonFile.class);
				
				TimesReader.getTimesReaderInstance().setConfiguredTime(timesFormatInJsonFile.getConfiguredTime());
				TimesReader.getTimesReaderInstance().setTimesMapping(timesFormatInJsonFile.getTimesMapping());
			}
		} catch (FileNotFoundException e) { 
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}	
	}
	
}