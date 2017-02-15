package gr.uoa.di.madgik.datatransformation.harvester.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gr.uoa.di.madgik.datatransformation.harvester.core.status.DataProviderStatus;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.AdditionalInfoForHarvestProcess;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.AdditionalInfoForRepo;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.RetryAfter;

public class MessageForEveryDataProvider {

	private boolean forceReharvest = false;
	private String schemaSupportedForUrl = null;
	private Boolean toDelete = false;
	private Boolean executing = false;
	private DataProviderStatus status = null;
	private String statusMessage = "";
	private Date lastHarvestingTime = null;
	private Message infoForHarvesting = null;
	private String metadataPrefix = null;
	private int sizeOfList = 0;
	private AdditionalInfoForHarvestProcess addInfoHP = null;
	private AdditionalInfoForRepo addInfoR = null;
	private RetryAfter retryAfter = null;
	private String collectionID = null;
	private List<String> locations = new ArrayList<String>();
	
	public boolean isForceReharvest() {
		return forceReharvest;
	}
	public void setForceReharvest(boolean forceReharvest) {
		this.forceReharvest = forceReharvest;
	}
	
	public String getSchemaSupportedForUrl() {
		return schemaSupportedForUrl;
	}
	public void setSchemaSupportedForUrl(String schemaSupportedForUrl) {
		this.schemaSupportedForUrl = schemaSupportedForUrl;
	}
	public Boolean getToDelete() {
		return toDelete;
	}
	public void setToDelete(Boolean toDelete) {
		this.toDelete = toDelete;
	}
	
	public Boolean isExecuting() {
		return executing;
	}
	public void setExecuting(Boolean executing) {
		this.executing = executing;
	}
	
	public DataProviderStatus getStatus() {
		return status;
	}
	public void setStatus(DataProviderStatus status) {
		this.status = status;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	public Date getLastHarvestingTime() {
		return lastHarvestingTime;
	}
	public void setLastHarvestingTime(Date lastHarvestingTime) {
		this.lastHarvestingTime = lastHarvestingTime;
	}
	
	public Message getInfoForHarvesting() {
		return infoForHarvesting;
	}
	public void setInfoForHarvesting(Message infoForHarvesting) {
		this.infoForHarvesting = infoForHarvesting;
	}
	
	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}
	
	public int getSizeOfList() {
		return sizeOfList;
	}
	public void setSizeOfList(int sizeOfList) {
		this.sizeOfList = sizeOfList;
	}
	public void addToSizeOfList(int sizeOfList) {
		this.sizeOfList += sizeOfList;
	}
	
	public AdditionalInfoForHarvestProcess getAddInfoHP() {
		return addInfoHP;
	}
	public void setAddInfoHP(AdditionalInfoForHarvestProcess addInfoHP) {
		this.addInfoHP = addInfoHP;
	}
	
	public AdditionalInfoForRepo getAddInfoR() {
		return addInfoR;
	}
	public void setAddInfoR(AdditionalInfoForRepo addInfoR) {
		this.addInfoR = addInfoR;
	}
	
	public RetryAfter getRetryAfter() {
		return retryAfter;
	}
	public void setRetryAfter(RetryAfter retryAfter) {
		this.retryAfter = retryAfter;
	}
	
	public String getCollectionID() {
		return collectionID;
	}
	public void setCollectionID(String collectionID) {
		this.collectionID = collectionID;
	}
	
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
}