package gr.uoa.di.madgik.datatransformation.harvester.core.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public interface ManagerOfHarvested {

	public void storeHarvested(HarvestedInfoObject harvestedInfoObject) throws IOException, SQLException ;
	public void deleteHarvested(Set<String> collectionIds) throws Exception;
	
}