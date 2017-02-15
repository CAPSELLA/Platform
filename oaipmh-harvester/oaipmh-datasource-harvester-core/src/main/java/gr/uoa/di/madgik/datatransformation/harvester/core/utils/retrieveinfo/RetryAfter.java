package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;

import java.util.concurrent.TimeUnit;

public class RetryAfter {

	private Integer time = 0;
	private TimeUnit timeUnit = null;
	
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

}