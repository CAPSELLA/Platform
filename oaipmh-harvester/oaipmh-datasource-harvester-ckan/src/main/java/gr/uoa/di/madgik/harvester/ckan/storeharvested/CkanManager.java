package gr.uoa.di.madgik.harvester.ckan.storeharvested;

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

	public CkanManager() {

		InputStream stream = null;
		try {
			stream = CkanManager.class.getClassLoader().getResourceAsStream("ckan.properties");
		} catch (Exception e) {
			logger.fatal("cannot find repository.properties file");
		}
		
		Properties props = new Properties();

		try {
			props.load(stream);
		} catch (IOException ex) {
			logger.error("failed to load the properties file", ex);

		}
		this.url = props.getProperty("url").trim();
		this.apiKey = props.getProperty("apiKey").trim();
		this.organization = props.getProperty("organization").trim();

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

	public void deleteDataset(String id) {
		try {
			client.deleteDataset(id);
		} catch (JackanException j) {
			logger.error("error while deleting dataset in ckan");
		}
	}
}
