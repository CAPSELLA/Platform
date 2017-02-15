package gr.uoa.di.madgik.datatransformation.harvester.harvesting;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.RetrievedNodes;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListMetadataFormats;
import gr.uoa.di.madgik.datatransformation.harvester.core.status.DataProviderStatus;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.RetryAfter;

import com.sun.jersey.api.client.ClientHandlerException;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.RegisteredRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.WriteUrls;
import gr.uoa.di.madgik.datatransformation.harvester.harvestedmanagement.core.SelectedDatabase;
import gr.uoa.di.madgik.datatransformation.harvester.requestedtypes.errorcodes.ErrorCode;
import gr.uoa.di.madgik.datatransformation.harvester.responsesofservice.DataErrorResponse;
import gr.uoa.di.madgik.datatransformation.harvester.utils.DataPublisherResponse;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetCurrentDateTimestamp;
import gr.uoa.di.madgik.datatransformation.harvester.utils.RequestData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.container.ErrorOnData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo.RetrieveError;
import gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo.RetrieveInfoByVerb;
import gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo.RetrieveMetadataFormats;
import gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo.RetrieveResumptionToken;

public class FetchData {
	
	private final static Logger logger = Logger.getLogger(FetchData.class);
	
	public static void fetchData(MessageForEveryDataProvider msg, Map<String, String> parameters) throws InterruptedException {
		Map<String, String> tempContainerParams = new HashMap<String, String>();
	
		if (parameters.get("resumptionToken")!=null) {
			for (Map.Entry<String, String> param: parameters.entrySet()) 
				tempContainerParams.put(param.getKey(), param.getValue());
			
			/** resumptionToken must be requested exclusive **/
			parameters.clear();
			parameters.put("verb", tempContainerParams.get("verb"));
			parameters.put("resumptionToken", tempContainerParams.get("resumptionToken"));
		}
		
		DataErrorResponse dr = fetchDataFromPublisher(msg, parameters, tempContainerParams);
		if (dr!=null) {
			if (dr.getResponseOk()!=null) {
				msg.setStatus(DataProviderStatus.SUCCESSFUL);
				msg.setStatusMessage("Execution succeed.");
				if (msg.getToDelete())
					RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
				else RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			}
		} else /*if (dr==null)*/ {
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
		}
	}
	
	private static DataErrorResponse fetchResponse(MessageForEveryDataProvider msg, Map<String, String> parameters, Map<String, String> tempContainerParams) {
		String body = null;
		try {
			DataPublisherResponse rm = null;
			try {
				if (msg.getLocations().isEmpty()) {
					rm = RequestData.requestHarvest(msg.getInfoForHarvesting().getUrl(), parameters);
					if (!rm.getLocations().isEmpty()) {
						do {
							msg.setLocations(rm.getLocations());
							rm = RequestData.requestHarvest(msg.getLocations().get(0), parameters);
						} while(!rm.getLocations().isEmpty());
					}
				} else rm = RequestData.requestHarvest(msg.getLocations().get(0), parameters);
				
			} catch (IllegalStateException r) {
				return null;
			}
			body = rm.getBody();
			RetryAfter rf = null;
			ErrorOnData errorOnData;
			if (rm.getErrorCode()==-2) {
				msg.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
				msg.setStatusMessage("Data publisher is not available");
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
				if (msg.getToDelete()) {
					RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
					return null;
				}				
				return new DataErrorResponse(rm, parameters.get("resumptionToken"));
			} else if ((rm.getErrorCode()!=0 && rm.getErrorMessage()!=null) && rm.getErrorCode()!=503) { /* data publisher's problem */
				msg.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
				if (rm.getErrorMessage()!=null && !rm.getErrorMessage().isEmpty())
					msg.setStatusMessage(rm.getErrorMessage());
				else msg.setStatusMessage("Data publisher faces an uknown error");
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
				if (msg.getToDelete()) {
					RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
					return null;
				}
				return new DataErrorResponse(rm, parameters.get("resumptionToken"));
			} else if (rm.getErrorCode()==503) {
				if (rm.getRetryAfter()!=0) {
					RetryAfter retry = new RetryAfter();
					retry.setTime(rm.getRetryAfter());
					retry.setTimeUnit(TimeUnit.SECONDS);
					msg.setRetryAfter(retry);
					msg.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
					msg.setStatusMessage("Data publisher is unavailable for about " + rm.getRetryAfter() + "  " + TimeUnit.SECONDS.toString());
				
					RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
					if (msg.getToDelete()) {
						RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
						return null;
					}	
					return new DataErrorResponse(retry, parameters.get("resumptionToken"));
				} else {
					msg.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
					msg.setStatusMessage(rm.getErrorMessage());
				
					RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
					if (msg.getToDelete()) {
						RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
						return null;
					}
					return new DataErrorResponse(rm, parameters.get("resumptionToken"));
				}
			}else if ((rf = checkRetryAfter(body))!=null) {
				msg.setRetryAfter(rf);
				msg.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
				msg.setStatusMessage("Data publisher is unavailable for about " + rf.getTime() + "  " + rf.getTimeUnit().toString().toLowerCase());
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
				if (msg.getToDelete()) {
					RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
					return null;
				}					
				return new DataErrorResponse(rf, parameters.get("resumptionToken"));
			} else if ((errorOnData = RetrieveError.retrieveError(body))!=null) {
				logger.info("url " + msg.getInfoForHarvesting().getUrl() + "  (resumptionToken:" +parameters.get("resumptionToken")+ "   verb:"
						+ parameters.get("verb") + "   metadataPrefix:" + parameters.get("metadataPrefix")
 						+ ")   failed due to error code: " + errorOnData.getErrorCode() + 
						"(" + errorOnData.getErrorMessage()+")");
				msg.setStatus(DataProviderStatus.ERROR_ON_DATA);
				msg.setStatusMessage(errorOnData.getErrorCode() + ": " + errorOnData.getErrorMessage());
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
				if (msg.getToDelete()) {
					RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
					return null;
				}
				return new DataErrorResponse(errorOnData, parameters.get("resumptionToken"));
			}
		} catch (ClientHandlerException e) {
			logger.info(e.getMessage());
			DataPublisherResponse dpr = new DataPublisherResponse();
			dpr.setErrorCode(00);
			dpr.setErrorMessage("Unreachable data publisher");
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			if (msg.getToDelete()) {
				RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
				return null;
			}
			return new DataErrorResponse(dpr, parameters.get("resumptionToken"));
		} catch (Exception e) {
			logger.info(e.getMessage());
			DataPublisherResponse dpr = new DataPublisherResponse();
			dpr.setErrorCode(00);
			dpr.setErrorMessage("Unknown data publisher error");
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			if (msg.getToDelete()) {
				RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
				return null;
			}
			return new DataErrorResponse(dpr, parameters.get("resumptionToken"));
		}
		
		/** no error occured -- continue harvesting **/
		RetrievedNodes retrievedNodes=null;
		try {
			retrievedNodes = RetrieveInfoByVerb.getNodes(body);
		} catch (ParseException e) {
			logger.info(e.getMessage());
			
			DataPublisherResponse dpr = new DataPublisherResponse();
			dpr.setErrorCode(00);
			dpr.setErrorMessage("Unknown error on data from publisher -- Not successful parse");
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			if (msg.getToDelete()) {
				RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
				return null;
			}
			return new DataErrorResponse(dpr, parameters.get("resumptionToken"));
		}
		
		boolean firstTimeToDB = true;
		if (msg.getSizeOfList()!=0)
			firstTimeToDB = false;
		
		SelectedDatabase.getSelectedDatabaseInstance().storeToDB(msg, retrievedNodes, firstTimeToDB);
		
		String resumptionToken = RetrieveResumptionToken.retrieveResumptionToken(body, parameters.get("verb"));
		if (resumptionToken!=null && resumptionToken.isEmpty()) {
						
			resumptionToken = null;
			msg.setStatus(DataProviderStatus.SUCCESSFUL);
			msg.setStatusMessage("Execution succeed.");
			msg.setRetryAfter(null);
			msg.getInfoForHarvesting().getListRecords().setResumptionToken(null);
			java.util.Date date= new java.util.Date();
			msg.setLastHarvestingTime(new Timestamp(date.getTime()));
			msg.addToSizeOfList(retrievedNodes.getNodes().size());
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			return new DataErrorResponse("responseOk", resumptionToken);
		} else {
			msg.setStatus(DataProviderStatus.EXECUTING);
			msg.setStatusMessage("Executing.");
		}
		msg.getInfoForHarvesting().getListRecords().setResumptionToken(resumptionToken);
		msg.setRetryAfter(null);
		java.util.Date date= new java.util.Date();
		msg.setLastHarvestingTime(new Timestamp(date.getTime()));
		msg.addToSizeOfList(retrievedNodes.getNodes().size());
		
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			WriteUrls.getWriteUrlsInstance().writeToFile();
		}

		if (msg.getToDelete()) {
			RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(msg, false);
			RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(msg);
			return null;
		}			
		return new DataErrorResponse("responseOk", resumptionToken);
	}
		
	private static DataErrorResponse fetchDataFromPublisher(MessageForEveryDataProvider msg, Map<String, String> parameters, Map<String, String> tempContainerParams) {
		DataErrorResponse harvestResponse = fetchResponse(msg, parameters, tempContainerParams);
		if (harvestResponse==null) return null;
		if (harvestResponse.getResponseOk()!=null && harvestResponse.getResumptionToken()==null)
			return new DataErrorResponse("responseOk", null);
		/** resumption token expired **/
		if (harvestResponse.getErrorOnData()!=null && 
			harvestResponse.getErrorOnData().getErrorCode()!=null && 
			harvestResponse.getErrorOnData().getErrorCode().toUpperCase().equals(ErrorCode.BADRESUMPTIONTOKEN.toString())) {
			parameters.clear();
			for (Map.Entry<String, String> param: tempContainerParams.entrySet())
				parameters.put(param.getKey(), param.getValue());
			parameters.put("resumptionToken", null);
		} else if(harvestResponse.getResumptionToken()!=null) {
			parameters.clear();
			parameters.put("verb", "ListRecords");
			parameters.put("resumptionToken", harvestResponse.getResumptionToken());
		}		
		
		harvestResponse = fetchResponse(msg, parameters, tempContainerParams);
		if (harvestResponse==null) return null;
		if (harvestResponse.getErrorOnData()!=null || harvestResponse.getRetryAfter()!=null || harvestResponse.getDataPublisherResponse()!=null)
			return harvestResponse;

		if (harvestResponse.getResponseOk()!=null && harvestResponse.getResumptionToken()!=null && !harvestResponse.getResumptionToken().isEmpty()) {
			parameters.clear();
			parameters.put("verb", "ListRecords");
			do {
				parameters.put("resumptionToken", harvestResponse.getResumptionToken());
					
				harvestResponse = fetchResponse(msg, parameters, tempContainerParams);
				if (harvestResponse==null) return null;
				if (harvestResponse.getErrorOnData()!=null ||
					harvestResponse.getDataPublisherResponse()!=null ||
					harvestResponse.getRetryAfter()!=null)
					return harvestResponse;
				
			} while (harvestResponse.getResumptionToken()!=null && !harvestResponse.getResumptionToken().isEmpty());
		}
		return new DataErrorResponse("responseOk", null);
	}

	
	private static RetryAfter checkRetryAfter(String body) {
		RetryAfter rf = null;
		ErrorOnData errorResponse = RetrieveError.retrieveError(body);
		if (errorResponse!=null && errorResponse.getErrorCode()!=null && errorResponse.getErrorMessage()!=null) {
			if (errorResponse.getErrorCode().equals("503")) {
				String[] errorPieces = errorResponse.getErrorMessage().split(" ");
				if (errorPieces[0].equals("Retry-After")) {
					int time = Integer.parseInt(errorPieces[1]);
					String timeUnit = errorPieces[2];
					rf = new RetryAfter();
					rf.setTime(time);
					if (timeUnit.toUpperCase().equals("SECONDS"))
						rf.setTimeUnit(TimeUnit.SECONDS);
					else if (timeUnit.toUpperCase().equals("MINUTES"))
						rf.setTimeUnit(TimeUnit.MINUTES);
					else if (timeUnit.toUpperCase().equals("HOURS"))
						rf.setTimeUnit(TimeUnit.HOURS);
					else if (timeUnit.toUpperCase().equals("DAYS"))
						rf.setTimeUnit(TimeUnit.DAYS);
				}
			}
		}
		return rf;
	}
	
	/** @throws Exception 
	 * if return true, its supporting query with from-until**/
	public static boolean checkIfSupportsDateQueries(String url, String verb, String metadataPrefix, Date beforeLeastRecentlyDate, String granularity) throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("verb", verb);
		parameters.put("metadataPrefix", metadataPrefix);
		parameters.put("from", GetCurrentDateTimestamp.convertTo(beforeLeastRecentlyDate, granularity).toString());
		
		DataPublisherResponse rm = null;
		try {
			rm = RequestData.requestHarvest(url, parameters);
		} catch (IllegalStateException r) {
			return false;
		}
		if (rm.getErrorCode()!=200) return false;
		String body = rm.getBody();
		if (rm.getErrorCode()==0 && rm.getErrorMessage()==null) { /* no error for the publisher */
			ErrorOnData errorResponse = RetrieveError.retrieveError(body); /* error on metadata file? */
			if (errorResponse.getErrorCode()!=null && 
					ErrorCode.valueOf(errorResponse.getErrorCode().toUpperCase()).equals(ErrorCode.NORECORDSMATCH.toString())) {
				return false;
			} else return RetrieveInfoByVerb.getNodesFromBeforeLeastDate(body, beforeLeastRecentlyDate);
		}
		return true;
	}
	
	public static List<String> getSupportedSchemas(String url) throws Exception {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("verb", ListMetadataFormats.getVerb());
		
		DataPublisherResponse rm = null;
		try {
			rm = RequestData.requestHarvest(url, parameters);
			if (!rm.getLocations().isEmpty()) {
				List<String> locations = new ArrayList<String>();
				locations.add("locations"); 
				locations.addAll(rm.getLocations());
			}
		} catch (IllegalStateException r) {
			return null;
		}
		if (rm.getErrorCode()!=0) return null;
		List<String> allSchemas = RetrieveMetadataFormats.retrieveSchemas(rm.getBody());
		return allSchemas;
	}
}