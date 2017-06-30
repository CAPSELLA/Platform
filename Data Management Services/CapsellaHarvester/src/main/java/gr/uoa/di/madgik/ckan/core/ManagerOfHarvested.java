package gr.uoa.di.madgik.ckan.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public interface ManagerOfHarvested {

	public void storeHarvested(HarvestedInfoObject harvestedInfoObject) throws IOException, SQLException ;
	public void deleteHarvested(Set<String> collectionIds) throws Exception;
	
}