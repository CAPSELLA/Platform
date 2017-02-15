package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times;

import java.util.concurrent.TimeUnit;

public class DefaultTime {

	private int time = 2;
	private TimeUnit timeUnit = TimeUnit.DAYS;
	
	public DefaultTime() {}
	
	public DefaultTime(int time, TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
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
