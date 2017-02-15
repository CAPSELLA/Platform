package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;

public class AdditionalInfoForHarvestProcess {

	private boolean noRecords = false;
	private boolean supportsDateQuery = false;
	private boolean serviceUnavailable = false;
	
	public boolean getNoRecords() {
		return noRecords;
	}
	public void setNoRecords(boolean noRecords) {
		this.noRecords = noRecords;
	}
	
	public boolean getSupportsDateQuery() {
		return supportsDateQuery;
	}
	public void setSupportsDateQuery(boolean supportsDateQuery) {
		this.supportsDateQuery = supportsDateQuery;
	}
	
	public boolean getServiceUnavailable() {
		return serviceUnavailable;
	}
	public void setServiceUnavailable(boolean serviceUnavailable) {
		this.serviceUnavailable = serviceUnavailable;
	}
	
}