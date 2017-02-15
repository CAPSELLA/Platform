package gr.uoa.di.madgik.datatransformation.harvester.archivemanagement.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.harvestedmanagement.utils.SHAHashing;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.GetRecord;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListIdentifiers;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListMetadataFormats;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListRecords;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListSets;
import gr.uoa.di.madgik.datatransformation.harvester.core.status.DataProviderStatus;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.AdditionalInfoForHarvestProcess;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.AdditionalInfoForRepo;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.RetryAfter;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.RegisteredRequests;
import gr.uoa.di.madgik.datatransformation.harvester.harvesting.FetchData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.DataPublisherResponse;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;
import gr.uoa.di.madgik.datatransformation.harvester.utils.RequestData;
import gr.uoa.di.madgik.datatransformation.harvester.utils.retrieveinfo.RetrieveAdditionalInfo;

public class Harvesting implements Callable<MessageForEveryDataProvider> {
	private Map<String, String> parameters;
	private MessageForEveryDataProvider messageForEveryDataProvider;
	
	public Harvesting(MessageForEveryDataProvider messageToHarvest) throws Exception {
		this.messageForEveryDataProvider = messageToHarvest;
		this.parameters = new HashMap<String, String>();
		
		switch (messageToHarvest.getInfoForHarvesting().getVerb()) {
			case "ListRecords":
				ListRecords listRecords = messageToHarvest.getInfoForHarvesting().getListRecords();
				if (listRecords==null) throw new Exception("Not a valid requestType");
				if (listRecords.getFrom()!=null)
					this.parameters.put("from", listRecords.getFrom());
				if (listRecords.getMetadataPrefix()!=null)
					this.parameters.put("metadataPrefix", listRecords.getMetadataPrefix());

				if (listRecords.getResumptionToken()!=null)
					this.parameters.put("resumptionToken", listRecords.getResumptionToken());
				if (listRecords.getSet()!=null)
					this.parameters.put("set", listRecords.getSet());
				if (listRecords.getUntil()!=null)
					this.parameters.put("until", listRecords.getUntil());
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			case "GetRecord":
				GetRecord getRecord = messageToHarvest.getInfoForHarvesting().getGetRecord();
				if (getRecord==null) throw new Exception("Not a valid requestType");
				if (getRecord.getIdentifier()!=null)
					this.parameters.put("identifier", getRecord.getIdentifier());
				if (getRecord.getMetadataPrefix()!=null) {
					this.parameters.put("metadataPrefix", getRecord.getMetadataPrefix());
				}
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			case "Identify":
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			case "ListIdentifiers":
				ListIdentifiers listIdentifiers = messageToHarvest.getInfoForHarvesting().getListIdentifiers();
				if (listIdentifiers==null) throw new Exception("Not a valid requestType");
				if (listIdentifiers.getFrom()!=null)
					this.parameters.put("from", listIdentifiers.getFrom());
				if (listIdentifiers.getMetadataPrefix()!=null) {
					this.parameters.put("metadataPrefix", listIdentifiers.getMetadataPrefix());
				}
				if (listIdentifiers.getResumptionToken()!=null)
					this.parameters.put("resumptionToken", listIdentifiers.getResumptionToken());
				if (listIdentifiers.getSet()!=null)
					this.parameters.put("set", listIdentifiers.getSet());
				if (listIdentifiers.getUntil()!=null)
					this.parameters.put("until", listIdentifiers.getUntil());
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			case "ListMetadataFormats":
				ListMetadataFormats listMetadataFormats = messageToHarvest.getInfoForHarvesting().getListMetadataFormats();
				if (listMetadataFormats==null) throw new Exception("Not a valid requestType");
				if (listMetadataFormats.getIdentifier()!=null)
					this.parameters.put("identifier", listMetadataFormats.getIdentifier());
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			case "ListSets":
				ListSets listSets = messageToHarvest.getInfoForHarvesting().getListSets();
				if (listSets.getResumptionToken()!=null)
					this.parameters.put("resumptionToken", listSets.getResumptionToken());
				this.parameters.put("verb", messageToHarvest.getInfoForHarvesting().getVerb());
				break;
			default:
				this.parameters.put("verb", GetProperties.getPropertiesInstance().getDefaultVerb());
				this.parameters.put("metadataPrefix", GetProperties.getPropertiesInstance().getDefaultMetadataPrefix());
		}
	}
	
	@Override
	public MessageForEveryDataProvider call() throws InterruptedException, Exception {
		if (this.messageForEveryDataProvider.isExecuting()) {
			return null;
		}

		if (!RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(this.messageForEveryDataProvider, true))
			return null; /*iam executing already, so get rid of me*/
		
		// get Infos for each Repository
		if (this.messageForEveryDataProvider.getAddInfoR()==null) {
			if (this.messageForEveryDataProvider.getToDelete()) {
				RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(this.messageForEveryDataProvider);
				return null;
			}
			
			HashMap<String, String> params = new HashMap<>();
			params.put("verb", "Identify");
			DataPublisherResponse rm = null;
			try {
				if (this.messageForEveryDataProvider.getLocations().isEmpty())
					rm = RequestData.requestHarvest(this.messageForEveryDataProvider.getInfoForHarvesting().getUrl(), params);
				else rm = RequestData.requestHarvest(this.messageForEveryDataProvider.getLocations().get(0), params);
				
			} catch (IllegalStateException r) {
				return null;
			}
			if (rm.getBody()==null && rm.getErrorCode()==-1) {
				this.messageForEveryDataProvider.setExecuting(false);
				
				if (this.messageForEveryDataProvider.getAddInfoHP()==null)
					this.messageForEveryDataProvider.setAddInfoHP(new AdditionalInfoForHarvestProcess());
				
				this.messageForEveryDataProvider.getAddInfoHP().setServiceUnavailable(true);
				this.messageForEveryDataProvider.setStatusMessage(rm.getErrorMessage());
		
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(this.messageForEveryDataProvider, false);
					
				return null;
			}else if (rm.getErrorCode()==-2) {
				this.messageForEveryDataProvider.setStatus(DataProviderStatus.DATA_PUBLISHER_UNAVAILABLE);
				this.messageForEveryDataProvider.setStatusMessage("Data publisher is not available");
		
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(this.messageForEveryDataProvider, false);	
				return null;
			}else if (rm.getErrorCode()!=0 && rm.getErrorMessage()!=null) {
				if (rm.getRetryAfter()!=0) {
					RetryAfter ra = new RetryAfter();
					ra.setTime(rm.getRetryAfter());
					ra.setTimeUnit(TimeUnit.SECONDS);
					this.messageForEveryDataProvider.setRetryAfter(ra);
				}
				this.messageForEveryDataProvider.setStatusMessage(rm.getErrorMessage());
		
				RegisteredRequests.getRegisteredRequestsInstance().updateStatusMessagesForUrl(this.messageForEveryDataProvider, false);	
				return null;
			}
			String body = rm.getBody();
			if (rm.getErrorCode()==0 && rm.getErrorMessage()==null) {
				AdditionalInfoForRepo addInfoForR = RetrieveAdditionalInfo.retrieveAdditionalInfoOfRepo(body);
				if (addInfoForR.getRepositoryName()==null) {
					String url = this.messageForEveryDataProvider.getInfoForHarvesting().getUrl();
					String mPrefix = this.messageForEveryDataProvider.getInfoForHarvesting().getListRecords().getMetadataPrefix();
					
					this.messageForEveryDataProvider.setCollectionID(SHAHashing.getHashedValue((url.concat(mPrefix))));
				} else this.messageForEveryDataProvider.setCollectionID(SHAHashing.getHashedValue(addInfoForR.getRepositoryName()));
				this.messageForEveryDataProvider.setAddInfoR(addInfoForR);
			}
		}

		FetchData.fetchData(this.messageForEveryDataProvider, this.parameters);
		
		return messageForEveryDataProvider; 
	}
}
