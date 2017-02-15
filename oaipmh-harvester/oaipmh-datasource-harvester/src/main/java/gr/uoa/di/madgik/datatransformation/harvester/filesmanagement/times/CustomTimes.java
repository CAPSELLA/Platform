package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.util.concurrent.TimeUnit;

public class CustomTimes {

	private String url;
	private int time = 0;
	private TimeUnit timeUnit;
	
	public CustomTimes(String url, int time, TimeUnit timeUnit) {
		this.url = url;
		this.time = time;
		this.timeUnit = timeUnit;
	}
	
	public CustomTimes() {}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
}
