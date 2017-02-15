package gr.uoa.di.madgik.datatransformation.harvester.dataservice.contextinit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

/**
 * This class initializes all the files needed by the harvester to operate  
 */
public class FilesSetup implements javax.servlet.ServletContextListener {

	
	private final static Logger logger = Logger.getLogger(FilesSetup.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//create empty times file
		File timesFile = new File(GetProperties.getPropertiesInstance().getTimesFile());
		try{
			if (!timesFile.exists()){
				FileWriter fileWriter = new FileWriter(timesFile);
				timesFile.createNewFile();
				fileWriter.write("{\"configuredTime\":{\"time\":10,\"timeUnit\":\"DAYS\"},\"timesMapping\":[]}");
				fileWriter.flush();
				fileWriter.close();
			}
		}
		catch(IOException ex){
			logger.error("Could not create times file at given path: "+GetProperties.getPropertiesInstance().getTimesFile());
		}
		
		//create empty archives file
		File archivesFile = new File(GetProperties.getPropertiesInstance().getArchivesFile());
		try{
			if (!archivesFile.exists()){
				FileWriter fileWriter = new FileWriter(archivesFile);
				archivesFile.createNewFile();
				fileWriter.write("[]");
				fileWriter.flush();
				fileWriter.close();
			}
		}
		catch(IOException ex){
			logger.error("Could not create archives file at given path: "+GetProperties.getPropertiesInstance().getArchivesFile());
		}
		
		//create empty queued file
		File queuedFile = new File(GetProperties.getPropertiesInstance().getQueuedFile());
		try{
			if (!queuedFile.exists()){
				FileWriter fileWriter = new FileWriter(queuedFile);
				queuedFile.createNewFile();
				fileWriter.write("[]");
				fileWriter.flush();
				fileWriter.close();
			}
		}
		catch(IOException ex){
			logger.error("Could not create queued file at given path: "+GetProperties.getPropertiesInstance().getQueuedFile());
		}
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
