package gr.uoa.di.madgik.datatransformation.harvester.archivemanagement.threads;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.QueuedRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.WriteUrls;

public class ManageFiles {
	private final static Logger logger = Logger.getLogger(SchedulingOfHarvestProcess.class);
	
	public void mergeQueueWithArchive() {
		try {
			synchronized (QueuedRequests.class) {
				RWActions.readFromFile(new gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.ReadUrls(), false);
				for (Map.Entry<String, List<MessageForEveryDataProvider>> request: QueuedRequests.getQueuedRequestsInstance().getQueuedRequestsMapping().entrySet()) {
					for (MessageForEveryDataProvider m: request.getValue())
						RWActions.registerUriOnFile(gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.WriteUrls.getWriteUrlsInstance(), m);
				}
				QueuedRequests.getQueuedRequestsInstance().getQueuedRequestsMapping().clear();
				
				synchronized (WriteUrls.class) {
					RWActions.writeToFile(new WriteUrls());
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}
	
}