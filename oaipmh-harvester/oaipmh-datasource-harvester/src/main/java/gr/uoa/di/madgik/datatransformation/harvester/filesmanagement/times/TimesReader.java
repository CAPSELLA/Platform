package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;

public class TimesReader {

	private static TimesReader timesReader = null;
	
	private DefaultTime configuredTime = null;
	/** Map<URL, CustomTimes> **/ 
	private Map<String, CustomTimes> timesMapping = null;
	
	public  static synchronized TimesReader getTimesReaderInstance() {
		if (timesReader==null) {
			timesReader = new TimesReader();
			RWActions.readFromFile(new ReadUrls(), false);
		}
		return timesReader;
	}
	
	protected TimesReader() {
		this.timesMapping = Collections.synchronizedMap(new HashMap<String, CustomTimes>());
		this.configuredTime = new DefaultTime();
	}
	
	public synchronized void setConfiguredTime(DefaultTime time) {
		synchronized (this.timesMapping) {
			this.configuredTime = time;
		}
	}
	
	public synchronized DefaultTime getConfiguredTime() {
		synchronized (this.timesMapping) {
			return this.configuredTime;
		}
	}
	
	public synchronized void addToTimesMapping(CustomTimes value) {
		synchronized (this.timesMapping) {
			this.timesMapping = (this.timesMapping==null? this.timesMapping = new HashMap<>(): this.timesMapping);
			
			this.timesMapping.put(value.getUrl(), value);
		}
	}
	
	public synchronized void removeFromTimesMapping( String url) {
		synchronized (this.timesMapping) {
			if (this.timesMapping!=null)
				if (this.timesMapping.remove(url)!=null)
					return;
		}		
	}
	
	public synchronized CustomTimes getFromTimesMapping(String url) {
		synchronized (this.timesMapping) {
			return (this.timesMapping!=null? this.timesMapping.get(url): null);
		}
	}
	

	public synchronized  Map<String, CustomTimes> getTimesMapping() {
		synchronized (this.timesMapping) {
			return this.timesMapping;
		}
	}
	
	public synchronized void setTimesMapping( Map<String, CustomTimes>timesMapping) {
		synchronized (this.timesMapping) {
				this.timesMapping = timesMapping;
		}
	}

}
