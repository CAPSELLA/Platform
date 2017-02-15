package gr.uoa.di.madgik.datatransformation.harvester.archivemanagement.threads;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo.RetryAfter;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.RegisteredRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.CustomTimes;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.DefaultTime;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.TimesReader;

public class CoordinateHarvestingProcess {
	
private final static Logger logger = Logger.getLogger(CoordinateHarvestingProcess.class);
	
	public static void executeHarvesting(ExecutorService executor) {
		try {
			/*List<urisToDelete>*/
			Set<String> urlsToDelete = new HashSet <String>();
			Map<String, List<MessageForEveryDataProvider>> m = RegisteredRequests.getRegisteredRequestsInstance().getRegisteredRequestsMapping();
			for (Map.Entry<String, List<MessageForEveryDataProvider>> map: m.entrySet()) {
				DefaultTime dt = TimesReader.getTimesReaderInstance().getConfiguredTime();
				
				for (MessageForEveryDataProvider messageToHarvest: map.getValue()) {
					if (messageToHarvest.getToDelete()) {
						urlsToDelete = ((Set<String>) (urlsToDelete==null? new HashSet<>(): urlsToDelete));
						urlsToDelete.add(messageToHarvest.getInfoForHarvesting().getUrl());
						continue;
					}
					CustomTimes ct = TimesReader.getTimesReaderInstance().getFromTimesMapping( messageToHarvest.getInfoForHarvesting().getUrl());

					if ((ct!=null ? isTimeToRun(messageToHarvest, ct.getTime(), ct.getTimeUnit(), messageToHarvest.getRetryAfter()) : 
						isTimeToRun(messageToHarvest, dt.getTime(), dt.getTimeUnit(), messageToHarvest.getRetryAfter()))) {
							Callable<MessageForEveryDataProvider> harvesting = new Harvesting(messageToHarvest);
							executor.submit(harvesting);
					}
				}
			}
			if(!urlsToDelete.isEmpty())
				RegisteredRequests.getRegisteredRequestsInstance().hardRremoveFromRegisteredRequests(urlsToDelete);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}
	
	private static boolean isTimeToRun(MessageForEveryDataProvider msg, int time, TimeUnit timeUnit, RetryAfter retryAfter) {
		java.util.Date dt= new java.util.Date();
		Date currentDate = new Timestamp(dt.getTime());
		
		if (msg.isForceReharvest() && !msg.isExecuting()) {
			msg.setSizeOfList(0);
			return true;
		}
		
		if (msg.isExecuting())
			return false;
		
		if (msg.getInfoForHarvesting().getListRecords().getResumptionToken()!=null && 
				!msg.getInfoForHarvesting().getListRecords().getResumptionToken().isEmpty())
			return true;
		
		if (msg.getLastHarvestingTime()==null)
			return true;
		if (retryAfter!=null) {
			if ((currentDate.getTime() - msg.getLastHarvestingTime().getTime()) > retryAfter.getTimeUnit().toMillis(retryAfter.getTime())) {
				msg.setRetryAfter(null);
				return true;
			} else return false;
		}
	
		long diff = currentDate.getTime() - msg.getLastHarvestingTime().getTime();
		long storedTime = timeUnit.toMillis(time);
		
		if (msg.getInfoForHarvesting().getListRecords().getResumptionToken()!=null)
			return true;
		if ((diff>storedTime && !msg.isExecuting()) ? true: false) {	
			if (!msg.getLocations().isEmpty())
				msg.getLocations().clear();

			msg.setSizeOfList(0);
			return true;
		} else return false;
	}
	
}
