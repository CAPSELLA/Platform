package gr.uoa.di.madgik.harvester.ckan.storeharvested;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanResource;
import eu.trentorise.opendata.jackan.model.CkanTag;
import gr.uoa.di.madgik.datatransformation.harvester.core.db.RetrievedNodes;

public class RecordManager {
	private String title;
	private String author;
	private String authorEmail;
	private String maintainer;
	private String maintainerEmail;
	private String notes;
	private String license;
	private String url;
	private String tags;
	private List<String> extras;
	private String resourceName;
	private String resourceUrl;
	private String resourceDescription;
	private String resourceFormat;

	private Logger logger = Logger.getLogger(RecordManager.class.getName());

	public RecordManager(String file) {
		logger.debug("RecordManager");

		InputStream stream = null;
		try {
			stream = RecordManager.class.getClassLoader().getResourceAsStream(file);
		} catch (Exception e) {
			logger.fatal("cannot find repository.properties file");
		}

		Properties props = new Properties();

		try {
			props.load(stream);
		} catch (IOException ex) {
			logger.error("failed to load the properties file", ex);

		}

		this.title = props.getProperty("Title").trim();
		this.author = props.getProperty("Author").trim();
		this.authorEmail = props.getProperty("AuthorEmail").trim();
		this.maintainer = props.getProperty("Maintainer").trim();
		this.maintainerEmail = props.getProperty("MaintainerEmail").trim();
		this.notes = props.getProperty("Notes").trim();
		this.license = props.getProperty("License").trim();
		this.url = props.getProperty("Url").trim();
		this.tags = props.getProperty("Tags").trim();
		this.extras = new ArrayList<String>();

		for (String extra : props.getProperty("Extras").split(",")) {
			this.extras.add(extra);
		}
		this.resourceName = props.getProperty("ResourceName").trim();
		this.resourceUrl = props.getProperty("ResourceUrl").trim();
		this.resourceDescription = props.getProperty("ResourceDescription").trim();
		this.resourceFormat = props.getProperty("ResourceFormat").trim();
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void storeRecords(CkanManager ckanManager, RetrievedNodes retrievedNodes) {
		logger.debug("storeRecords");

		Map<String, Node> nodes = retrievedNodes.getNodes();
		Set<String> nodesToDelete = retrievedNodes.getNodesToDelete();

		for (Map.Entry<String, Node> entry : nodes.entrySet()) {
			String id = entry.getKey().replaceAll("[^-a-zA-Z0-9]", "-").toLowerCase();

			if (nodesToDelete.contains(id)) // dataset is to be deleted
				continue;

			if (ckanManager.containsDataset(id))// dataset already exists
				continue;

			// create dataset to insert in ckan
			Node node = entry.getValue();

			CkanDataset dataset = new CkanDataset();

			dataset.setId(id);
			dataset.setName(id);
			if (((Element) node).getElementsByTagName(title).getLength() > 0) {
				dataset.setTitle(((Element) node).getElementsByTagName(title).item(0).getTextContent());
			}

			if (((Element) node).getElementsByTagName(author).getLength() > 0)
				dataset.setAuthor(((Element) node).getElementsByTagName(author).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(authorEmail).getLength() > 0)
				dataset.setAuthorEmail(((Element) node).getElementsByTagName(authorEmail).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(maintainer).getLength() > 0)
				dataset.setMaintainer(((Element) node).getElementsByTagName(maintainer).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(maintainerEmail).getLength() > 0)
				dataset.setMaintainerEmail(
						((Element) node).getElementsByTagName(maintainerEmail).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(notes).getLength() > 0)
				dataset.setNotes(((Element) node).getElementsByTagName(notes).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(license).getLength() > 0)
				dataset.setLicenseTitle(((Element) node).getElementsByTagName(license).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(url).getLength() > 0)
				dataset.setUrl(((Element) node).getElementsByTagName(url).item(0).getTextContent());

			if (((Element) node).getElementsByTagName(tags).getLength() > 0) {
				List<CkanTag> tagList = new ArrayList<CkanTag>();
				for (int i = 0; i < ((Element) node).getElementsByTagName(tags).getLength(); ++i) {
					CkanTag tag = new CkanTag();
					tag.setDisplayName(((Element) node).getElementsByTagName(tags).item(i).getTextContent());
					tagList.add(tag);
				}
				dataset.setTags(tagList);
			}

			if (((Element) node).getElementsByTagName(resourceUrl).getLength() > 0) {
				List<CkanResource> resources = new ArrayList<CkanResource>();
				for (int i = 0; i < ((Element) node).getElementsByTagName(resourceUrl).getLength(); ++i) {
					CkanResource resource = new CkanResource();
					resource.setUrl(((Element) node).getElementsByTagName(resourceUrl).item(i).getTextContent());
					if (resource.getUrl().contains(".")) {
						if (((Element) node).getElementsByTagName(resourceFormat).getLength() > i) {
							resource.setFormat(
									((Element) node).getElementsByTagName(resourceFormat).item(i).getTextContent());

						} else {
							logger.error("There is no resource format");
						}
					}
					if (((Element) node).getElementsByTagName(resourceName).getLength() > i)
						resource.setName(((Element) node).getElementsByTagName(resourceName).item(i).getTextContent());
					if (((Element) node).getElementsByTagName(resourceDescription).getLength() > i)
						resource.setDescription(
								((Element) node).getElementsByTagName(resourceDescription).item(i).getTextContent());
					resources.add(resource);
				}
				dataset.setResources(resources);
			}
			ckanManager.addDataset(dataset);
		}
		return;
	}
}
