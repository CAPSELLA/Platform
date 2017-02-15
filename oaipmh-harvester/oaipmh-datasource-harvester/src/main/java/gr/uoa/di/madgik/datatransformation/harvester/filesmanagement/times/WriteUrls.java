package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

public class WriteUrls {

	private final static Logger logger = Logger.getLogger(WriteUrls.class);
	
	public static void registerUriOnFile(CustomTimes ct) {
		synchronized (TimesReader.getTimesReaderInstance()) {
			RWActions.readFromFile(new ReadUrls(), false);
			TimesReader.getTimesReaderInstance().addToTimesMapping( ct);
			writeToFile();
			RWActions.readFromFile(new ReadUrls(), false);
		}
	}
	
	public static void writeToFile() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			TimesFormatInJsonFile timesFormatInJsonFile = new TimesFormatInJsonFile();
			timesFormatInJsonFile.setConfiguredTime(TimesReader.getTimesReaderInstance().getConfiguredTime());
			timesFormatInJsonFile.setTimesMapping(TimesReader.getTimesReaderInstance().getTimesMapping());
			objectMapper.writeValue(new File(GetProperties.getPropertiesInstance().getTimesFile()), timesFormatInJsonFile);
		} catch (JsonGenerationException e) {
			logger.info(e.getMessage());
		} catch (JsonMappingException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}
	
}
