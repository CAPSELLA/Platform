package gr.uoa.di.madgik.datatransformation.harvester.requestmanagement;

import java.util.Map;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.WriteUrls;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;

public class RequestManager {

	public static void proceedToRegister(MessageForEveryDataProvider messageForEveryDataProvider)  throws Exception  {
		synchronized (HarvestingOnExecution.class) {
			if (HarvestingOnExecution.getHarvestingInstance().getInExecution())
				RWActions.registerUriOnFile(new gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.WriteUrls(), messageForEveryDataProvider);
			else RWActions.registerUriOnFile(WriteUrls.getWriteUrlsInstance(), messageForEveryDataProvider);
		}
	}

	public static void proceedToRegister(Map<String, String> parameters) throws Exception {
		synchronized (HarvestingOnExecution.class) {
			if (HarvestingOnExecution.getHarvestingInstance().getInExecution())
				RWActions.registerUriOnFile(new gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.WriteUrls(), parameters);
			else RWActions.registerUriOnFile(WriteUrls.getWriteUrlsInstance(), parameters);
		}
	}
}
