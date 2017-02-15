package gr.uoa.di.madgik.datatransformation.harvester.utils;

import java.util.ArrayList;
import java.util.List;

public class DataPublisherResponse {
	
	private String body;
	private int errorCode = 0;
	private String errorMessage;
	
	private int retryAfter;
	
	/*locations due to 301 error*/
	private List<String> locations = new ArrayList<String>();
	
	public DataPublisherResponse() {}
	
	public DataPublisherResponse(String body, int errorCode, String errorMessage) {
		this.body = body;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public DataPublisherResponse(List<String> locations, int errorCode, String errorMessage) {
		this.locations = locations;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getRetryAfter() {
		return retryAfter;
	}
	public void setRetryAfter(int retryAfter) {
		this.retryAfter = retryAfter;
	}

	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
}
