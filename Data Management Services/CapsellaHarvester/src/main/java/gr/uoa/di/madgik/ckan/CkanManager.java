package gr.uoa.di.madgik.ckan;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.trentorise.opendata.jackan.CheckedCkanClient;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.exceptions.JackanException;
import eu.trentorise.opendata.jackan.model.CkanDataset;


public class CkanManager {
	
	private static CkanClient client;
	private String url;
	private String apiKey;
	private String organization;
	private Logger logger = Logger.getLogger(CkanManager.class.getName());

	public CkanManager(String url, String apiKey, String organization) {

		this.url = url;
		this.apiKey = apiKey;
		this.organization = organization;
		
		createClient();
	}

	public void createClient() {
		client = new CheckedCkanClient(url, apiKey);
	}

	public Boolean containsDataset(String id) {
		try {
			client.getDataset(id);
		} catch (JackanException j) {
			logger.debug("there is no dataset with id = " + id);
			return false;
		}
		return true;
	}

	public void addDataset(CkanDataset dataset) {
		try {
			if (this.organization == null) {
				logger.error("organization is null");
			}
			dataset.setOwnerOrg(this.organization);
			client.createDataset(dataset);

		} catch (JackanException j) {
			logger.error(j.getMessage());
		}
	}

	public void updateDataset(CkanDataset dataset) {
		try {
			if (this.organization == null) {
				logger.error("organization is null");
			}
			dataset.setOwnerOrg(this.organization);
			client.updateDataset(dataset);

		} catch (JackanException j) {
			logger.error(j.getMessage());
		}
	}
	
	public void deleteDataset(String id) {
		try {
			client.deleteDataset(id);
		} catch (JackanException j) {
			logger.error("error while deleting dataset in ckan");
		}
	}

}
