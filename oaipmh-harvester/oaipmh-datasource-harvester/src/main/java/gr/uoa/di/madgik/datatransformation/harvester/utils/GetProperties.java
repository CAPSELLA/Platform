package gr.uoa.di.madgik.datatransformation.harvester.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
	
	private static GetProperties instance = null;
	
	private String serviceUrlJson = null;
	private String serviceUrlParameters = null;
	private String newServiceUrlParameters = null;
	private String deleteServiceUrlParameters =null;
	private String archivesFile = null;
	private String queuedFile = null;
	private String timesFile = null;
	private String timeForScheduler = null;
	private String timeUnitForScheduler = null;
	private String timesDir = null;
	private String defaultVerb = null;
	private String defaultMetadataPrefix = null;
	private String classNameForDB = null;

	
	protected GetProperties() {
		getProperties();
	}
	
	public static GetProperties getPropertiesInstance() {
		if (instance == null) 
			instance = new GetProperties();
		return instance;
	}

	
	public String getServiceUrlJson() {
		return this.serviceUrlJson;
	}
	
	public String getServiceUrlParameters() {
		return this.serviceUrlParameters;
	}
	
	public String getNewServiceUrlParameters() {
		return this.newServiceUrlParameters;
	}
	
	public String getDeleteServiceUrlParameters() {
		return this.deleteServiceUrlParameters;
	}
	
	public String getArchivesFile() {
		return this.archivesFile;
	}

	public String getQueuedFile() {
		return this.queuedFile;
	}

	public String getTimesFile() {
		return this.timesFile;
	}
	
	public String getTimeForScheduler() {
		return this.timeForScheduler;
	}
	
	public String getTimeUnitForScheduler() {
		return this.timeUnitForScheduler;
	}
	
	public String getTimesDir() {
		return this.timesDir;
	}
	
	public String getDefaultVerb() {
		return this.defaultVerb;
	}
	
	public String getDefaultMetadataPrefix() {
		return this.defaultMetadataPrefix;
	}

	public String getClassNameForDB() {
		return classNameForDB;
	}

	private void getProperties() {
		InputStream input = null;
		try {
			Properties prop = new Properties();
			input = GetProperties.class.getClassLoader().getResourceAsStream("harvester.properties");
			prop.load(input);
			this.serviceUrlJson = prop.getProperty("service_url_json");
			this.serviceUrlParameters = prop.getProperty("service_url_parameters");
			this.newServiceUrlParameters = prop.getProperty("new_service_url_parameters");
			this.deleteServiceUrlParameters = prop.getProperty("delete_service_url_parameters");
			this.archivesFile = prop.getProperty("archives");
			this.queuedFile = prop.getProperty("queued");
			this.timesFile = prop.getProperty("times");
			this.timeForScheduler = prop.getProperty("time_for_scheduler");
			this.timeUnitForScheduler = prop.getProperty("time_unit_for_scheduler");
			this.timesDir = prop.getProperty("times_dir");
			this.defaultVerb = prop.getProperty("default_verb");
			this.defaultMetadataPrefix = prop.getProperty("default_metadataPrefix");
			this.classNameForDB = prop.getProperty("gr_cite_harvester_name_of_class_for_db");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
