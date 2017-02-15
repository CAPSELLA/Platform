package gr.uoa.di.madgik.datatransformation.harvester.harvestedmanagement.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;
import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.HarvestedInfoObject;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.ManagerOfHarvested;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.RetrievedNodes;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;
import gr.uoa.di.madgik.harvester.ckan.harvestedmanagement.StoreToCkan;

public class SelectedDatabase {
	
	private final static Logger logger = Logger.getLogger(SelectedDatabase.class);
	
	private static SelectedDatabase selectedDB;
	
	private ManagerOfHarvested selectedDbImplementation = null;
	
	protected SelectedDatabase() {
		String className = GetProperties.getPropertiesInstance().getClassNameForDB();

		if (className.equals("StoreToCkan")) {			
			selectedDbImplementation = (ManagerOfHarvested) StoreToCkan.getStoreToCkanInstance();
		}else{
			logger.error("Wrong name of class for db");
		}
	}
	
	public static SelectedDatabase getSelectedDatabaseInstance() {
		if (selectedDB==null)
			selectedDB = new SelectedDatabase();
		return selectedDB;
	}
	
	public void storeToDB(MessageForEveryDataProvider msg , RetrievedNodes retrievedNodes, Boolean firstTimeToStore) {
		try {
			HarvestedInfoObject obj = new HarvestedInfoObject();
			obj.setRetrievedNodes(retrievedNodes);
			obj.setCollectionID(msg.getCollectionID());
			obj.setUri(msg.getInfoForHarvesting().getUrl());
			obj.setVerb("ListRecords");
			obj.setMetadataPrefix(msg.getInfoForHarvesting().getListRecords().getMetadataPrefix());
			obj.setFirstTimeToStore(firstTimeToStore);
			this.selectedDbImplementation.storeHarvested(obj);
		} catch (IOException | SQLException e) {
			logger.info(e.getMessage()+"storeToDB");
		}
	}
	
	public void removeFromDB(Set<String> collectionIdsToDelete) {
		try {
			logger.info("removeFromDB");
			this.selectedDbImplementation.deleteHarvested(collectionIdsToDelete);
		} catch (Exception e) {
			logger.info("removeFromDB failed!!");
			logger.info(e.getMessage()+"removeFromDB");
		}
	}
	
	
	
	
}