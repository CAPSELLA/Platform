package gr.uoa.di.madgik.ckan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import gr.uoa.di.madgik.ckan.core.RetrievedNodes;
import gr.uoa.di.madgik.model.Endpoint;
import gr.uoa.di.madgik.model.Metadata;

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

	public static CkanDataset storeRecords(Metadata metadata) {

		CkanDataset dataset = new CkanDataset();

		dataset.setId(metadata.getUuid());
		dataset.setName(metadata.getDatasetName().toLowerCase());
		dataset.setTitle(metadata.getDatasetName());
		
		if (metadata.getAuthor() != null && metadata.getAuthor().length() > 0)
			dataset.setAuthor(metadata.getAuthor());
		else
		{
			dataset.setAuthor(metadata.getUsername());
		}
		
		if (metadata.getContentType().length() > 0)
			dataset.setType(metadata.getContentType());

		if (metadata.getOwnerGroup() != null && metadata.getOwnerGroup().length() > 0)
			dataset.setOwnerOrg(metadata.getOwnerGroup());

		
		if (metadata.getAccess() != null && metadata.getAccess().length() > 0)
		{
			if(metadata.getAccess().equals("public"))
			{
				dataset.setPriv(false);
				dataset.setOpen(true);
			}
			else
			{
				dataset.setPriv(true);
				dataset.setOpen(false);
			}
		}
		else
		{
			dataset.setPriv(false);
			dataset.setOpen(true);
		}
		
		
		if (metadata.getLastUpdated() != null)
			dataset.setRevisionTimestamp(metadata.getLastUpdated());
		
		
		if (metadata.getAuthorEmail() != null && metadata.getAuthorEmail().length() > 0)
			dataset.setAuthorEmail(metadata.getAuthorEmail());

		if (metadata.getMaintainer() != null && metadata.getMaintainer().length() > 0)
			dataset.setMaintainer(metadata.getMaintainer());

		if (metadata.getMaintainerEmail() != null && metadata.getMaintainerEmail().length() > 0)
			dataset.setMaintainerEmail(metadata.getMaintainerEmail());

		if (metadata.getDescription() != null && metadata.getDescription().length() > 0)	//description
			dataset.setNotes(metadata.getDescription());

		if (metadata.getLicense() != null && metadata.getLicense().length() > 0)
			dataset.setLicenseTitle(metadata.getLicense());

		if (metadata.getSource() != null && metadata.getSource().length() > 0)
			dataset.setUrl(metadata.getSource());

		
		if (metadata.getTags() != null && metadata.getTags().length() > 0)
		{
			List<String> tags = new ArrayList<>();
			
			if(metadata.getTags().contains(","))
				tags = Arrays.asList(metadata.getTags().split("\\s*,\\s*"));
			else
				tags.add(metadata.getTags());

			
			if (tags.size() > 0) {
				List<CkanTag> tagList = new ArrayList<CkanTag>();
				for (int i = 0; i < tags.size(); ++i) {
					CkanTag tag = new CkanTag();
					tag.setName(tags.get(i));
					tagList.add(tag);
				}
				dataset.setTags(tagList);
			}

		}
		
		ArrayList<String> urls = new ArrayList<String>();
		
		for(Endpoint endpoint : metadata.getEndpoint())
		{
			urls.add(endpoint.getEndpointUrl());

		}
		if (urls.size() > 0) {
			List<CkanResource> resources = new ArrayList<CkanResource>();
			for (int i = 0; i < urls.size(); ++i) {
				CkanResource resource = new CkanResource();
				resource.setUrl(urls.get(i));
				resource.setFormat(metadata.getContentType().toUpperCase());
				resource.setName(metadata.getDatasetName());
				resource.setDescription(metadata.getDescription());
				resources.add(resource);
			}
			dataset.setResources(resources);
		}

		return dataset;
	}

}
