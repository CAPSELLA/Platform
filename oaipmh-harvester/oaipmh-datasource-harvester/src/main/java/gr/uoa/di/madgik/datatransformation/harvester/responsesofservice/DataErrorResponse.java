package gr.uoa.di.madgik.datatransformation.harvester.responsesofservice;

import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.RetryAfter;

import gr.uoa.di.madgik.datatransformation.harvester.utils.DataPublisherResponse;
import gr.uoa.di.madgik.datatransformation.harvester.utils.container.ErrorOnData;

public class DataErrorResponse {

	private RetryAfter retryAfter = null;
	private ErrorOnData errorOnData = null;
	private DataPublisherResponse dataPublisherResponse = null;
	private String resumptionToken = null;
	private String responseOk = null;

	public DataErrorResponse() {}
	
	public DataErrorResponse(ErrorOnData errorResponse, String resumptionToken) {
		this.errorOnData = errorResponse;
		this.resumptionToken = resumptionToken;
	}
	public DataErrorResponse(DataPublisherResponse dataPublisherResponse, String resumptionToken) {
		this.dataPublisherResponse = dataPublisherResponse;
		this.resumptionToken = resumptionToken;
	}
	public DataErrorResponse(RetryAfter retryAfter, String resumptionToken) {
		this.retryAfter = retryAfter;
		this.resumptionToken = resumptionToken;
	}
	public DataErrorResponse(String responseOk, String resumptionToken) {
		this.responseOk = responseOk;
		this.resumptionToken = resumptionToken;
	}
	
	public RetryAfter getRetryAfter() {
		return retryAfter;
	}
	public void setRetryAfter(RetryAfter retryAfter) {
		this.retryAfter = retryAfter;
	}
	
	public ErrorOnData getErrorOnData() {
		return errorOnData;
	}
	public void setErrorOnData(ErrorOnData errorOnData) {
		this.errorOnData = errorOnData;
	}

	public DataPublisherResponse getDataPublisherResponse() {
		return dataPublisherResponse;
	}
	public void setDataPublisherResponse(DataPublisherResponse dataPublisherResponse) {
		this.dataPublisherResponse = dataPublisherResponse;
	}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}
	
	public String getResponseOk() {
		return responseOk;
	}
	public void setResponseOk(String responseOk) {
		this.responseOk = responseOk;
	}
	
}
