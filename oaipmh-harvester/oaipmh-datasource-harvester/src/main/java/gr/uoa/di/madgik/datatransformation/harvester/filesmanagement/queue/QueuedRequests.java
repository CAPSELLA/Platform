package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

public class QueuedRequests {

	public static QueuedRequests queuedRequests;
	
	private LinkedHashMap<String, List<MessageForEveryDataProvider>> queuedMapping = null;

	protected QueuedRequests(){
		this.queuedMapping = new LinkedHashMap<>();
	}
	
	public static synchronized QueuedRequests getQueuedRequestsInstance() {
		if (queuedRequests==null) {
			queuedRequests = new QueuedRequests();
		}
		return queuedRequests;
	}
	
	public List<MessageForEveryDataProvider> getFromQueuedRequestsMapping(String key) {
		return this.queuedMapping.get(key);
	}
	
	public LinkedHashMap<String, List<MessageForEveryDataProvider>> getQueuedRequestsMapping() {
		return this.queuedMapping;
	}
	
	public void setInRequestsMapping(String key, MessageForEveryDataProvider value) {
		synchronized (queuedMapping) {
			List<MessageForEveryDataProvider> list = queuedMapping.get(key);
			if (list == null) list = new ArrayList<MessageForEveryDataProvider>();
			if (!list.isEmpty())
				if (checkExistence(list, value)) 
					return;
			list.add(value);
			queuedMapping.put(key, list);
		}
	}
	
	private boolean checkExistence(List<MessageForEveryDataProvider> list, MessageForEveryDataProvider value) {
		for (MessageForEveryDataProvider m: list) {
			if (m.getInfoForHarvesting().getListRecords()!=null) {
				if (m.getInfoForHarvesting().getListRecords().getMetadataPrefix()!=null && 
					value.getInfoForHarvesting().getListRecords()!=null &&
					value.getInfoForHarvesting().getListRecords().getMetadataPrefix()!=null &&
					m.getInfoForHarvesting().getListRecords().getMetadataPrefix().equals(value.getInfoForHarvesting().getListRecords().getMetadataPrefix())) {
					return true;
				} 
			} 
		}
		return false;
	}
}
