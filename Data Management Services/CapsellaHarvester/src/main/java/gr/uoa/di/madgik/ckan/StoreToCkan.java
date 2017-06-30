package gr.uoa.di.madgik.ckan;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

import gr.uoa.di.madgik.ckan.core.HarvestedInfoObject;
import gr.uoa.di.madgik.ckan.core.ManagerOfHarvested;

public class StoreToCkan implements ManagerOfHarvested  {
	private static Logger logger = Logger.getLogger(StoreToCkan.class.getName());

	private CkanManager ckanManager;
	
	private static StoreToCkan storeToCkan = null;
	
	public static StoreToCkan getStoreToCkanInstance() {
		logger.debug("getStoreToCkanInstance");

		if (storeToCkan == null)
			storeToCkan = new StoreToCkan();
		return storeToCkan;
	}
	
	
	protected StoreToCkan() {
		//this.ckanManager = new CkanManager();
		
	}
	
	public void storeHarvested(HarvestedInfoObject harvestedInfoObject) throws IOException, SQLException {
		logger.debug("storeHarvested");

		if(!harvestedInfoObject.getVerb().equals("ListRecords"))
		{
			logger.error("Error this is not the right verb, "+harvestedInfoObject.getVerb());

			return;
		}
		
		RecordManager recordManager;
		
		if(harvestedInfoObject.getMetadataPrefix().equals("oai_dc"))
		{
		//	recordManager = new RecordManager("oai_dc.properties");
	//		recordManager.storeRecords(ckanManager, harvestedInfoObject.getRetrievedNodes());
			return;
		}
					
	}

	public void deleteHarvested(Set<String> collectionIds) throws Exception {
		logger.debug("deleteHarvested");
		if(collectionIds.isEmpty())
			logger.debug("collectionIds is empty!!!");

		for(String id : collectionIds)
		{
			ckanManager.deleteDataset(id);
		}
	}

}
