package gr.uoa.di.madgik.datatransformation.harvester.dataservice.servicecontrol;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

import com.sun.jersey.spi.resource.Singleton;

import gr.uoa.di.madgik.datatransformation.harvester.archivemanagement.threads.SchedulingOfHarvestProcess;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.ReadUrls;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;

@Singleton
@Path("/")
public class HarvesterService {
	
	private final static Logger logger = Logger.getLogger(HarvesterService.class);
	private static ScheduledExecutorService executor = null;
	
	public HarvesterService() {		
		/** initialize admin of all harvesting action **/

		synchronized (ReadUrls.class) {
			RWActions.readFromFile(new ReadUrls(), true);			
		}
		try {
			executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(SchedulingOfHarvestProcess.periodicTask(), 0, 
					Integer.parseInt(GetProperties.getPropertiesInstance().getTimeForScheduler()), 
					TimeUnit.valueOf(GetProperties.getPropertiesInstance().getTimeUnitForScheduler()));
		} catch (RuntimeException re) {
			logger.info(re.getMessage());
			
		}
	}
	
	@GET
	@Path("/toHarvest")
	@Produces(MediaType.TEXT_PLAIN)
	public String toHarvest() {
	     return "harvester page";
	}
}