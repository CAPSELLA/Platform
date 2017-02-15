package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger;

import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

public class RegisteredUrlsInfoMessenger {

	String uri = null;
	private int time = 0;
	private String timeUnit = null;
	List<MessageForEveryDataProvider> message = new ArrayList<MessageForEveryDataProvider>();
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public List<MessageForEveryDataProvider> getMessage() {
		return message;
	}
	public void setMessage(List<MessageForEveryDataProvider> message) {
		this.message = message;
	}
	
}