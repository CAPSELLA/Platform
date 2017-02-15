package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger;

public class NewServiceMessenger {

	private String newUri = null;
	private String schema = null;
	private Boolean defaultTime = true;
	private String newIntervalTime = null;
	private String newTimeUnit = null;
	
	public String getNewUri() {
		return newUri;
	}
	public void setNewUri(String newUri) {
		this.newUri = newUri;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public Boolean getDefaultTime() {
		return defaultTime;
	}
	public void setDefaultTime(Boolean defaultTime) {
		this.defaultTime = defaultTime;
	}
	
	public String getNewIntervalTime() {
		return newIntervalTime;
	}
	public void setNewIntervalTime(String newIntervalTime) {
		this.newIntervalTime = newIntervalTime;
	}
	
	public String getNewTimeUnit() {
		return newTimeUnit;
	}
	public void setNewTimeUnit(String newTimeUnit) {
		this.newTimeUnit = newTimeUnit;
	}	
}