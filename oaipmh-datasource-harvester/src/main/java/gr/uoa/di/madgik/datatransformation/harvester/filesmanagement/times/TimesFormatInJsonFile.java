package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *  This class is needed for deserialization of json format from times file.
 *  Times file contains an object that contains a configuredTime member and
 *  a list of custom times for each end point 
 * 
 * **/

public class TimesFormatInJsonFile {

	private DefaultTime configuredTime = null;
				/*  customTimes/per url */
	private  Map<String, CustomTimes> timesMapping = new HashMap<>();
	
	public DefaultTime getConfiguredTime() {
		return configuredTime;
	}
	public void setConfiguredTime(DefaultTime configuredTime) {
		this.configuredTime = configuredTime;
	}
	
	public Map<String, CustomTimes> getTimesMapping() {
		return timesMapping;
	}
	public void setTimesMapping(Map<String, CustomTimes> timesMapping) {
		this.timesMapping = timesMapping;
	}
	
}