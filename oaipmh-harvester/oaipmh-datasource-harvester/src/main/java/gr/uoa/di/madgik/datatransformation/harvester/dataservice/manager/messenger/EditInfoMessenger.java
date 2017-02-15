package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger;

public class EditInfoMessenger {

	private String url = null;
	private String time = null;
	private String timeUnit;
	
	private String defaultTime = null;
	private String defaultTimeUnit = null;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public String getDefaultTime() {
		return defaultTime;
	}
	public void setDefaultTime(String defaultTime) {
		this.defaultTime = defaultTime;
	}
	
	public String getDefaultTimeUnit() {
		return defaultTimeUnit;
	}
	public void setDefaultTimeUnit(String defaultTimeUnit) {
		this.defaultTimeUnit = defaultTimeUnit;
	}
	
}
