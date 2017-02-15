package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger;

public class InfoForPopulation {

	String name = null;
	Boolean status = null;
	String statusMessage = null;
	Integer numberOfRecords = 0;
	String intervalTime = null;
	String timeUnit = null;
	String lastHarvestingTime = null;
	
	String defaultTime = null;
	String defaultTimeUnit = null;
	
	/** Additional info **/
	String url = null;
	String schema = null;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	public Integer getNumberOfRecords() {
		return numberOfRecords;
	}
	public void setNumberOfRecords(Integer numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	
	public String getIntervalTime() {
		return intervalTime;
	}
	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}
	
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public String getLastHarvestingTime() {
		return lastHarvestingTime;
	}
	public void setLastHarvestingTime(String lastHarvestingTime) {
		this.lastHarvestingTime = lastHarvestingTime;
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
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
}