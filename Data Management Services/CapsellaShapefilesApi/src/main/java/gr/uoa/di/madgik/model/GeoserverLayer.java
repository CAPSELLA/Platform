package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GeoserverLayer implements Serializable
{
	private static final long serialVersionUID = 7787195504914478762L;

	private String id = null;
	private String type = null;
	private String defaultStyle = null;
	private ArrayList<String> styles = new ArrayList<String>();
	private boolean enabled = false;
	private String resource = null;
	private String resourceName = null;
	private String featureTypeLink = null;
	private String datastore = null;
	private String workspace = null;
	private String coveragestore = null;
	private String title = null;

	public String getDatastore()
	{
		return datastore;
	}

	public void setDatastore(String datastore)
	{
		this.datastore = datastore;
	}

	public String getWorkspace()
	{
		return workspace;
	}

	public void setWorkspace(String workspace)
	{
		this.workspace = workspace;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDefaultStyle()
	{
		return defaultStyle;
	}

	public void setDefaultStyle(String defaultStyle)
	{
		this.defaultStyle = defaultStyle;
	}

	public ArrayList<String> getStyles()
	{
		return styles;
	}

	public void setStyles(ArrayList<String> styles)
	{
		this.styles = styles;
	}

	public void addStyle(String style)
	{
		this.styles.add(style);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getResource()
	{
		return resource;
	}

	public void setResource(String resource)
	{
		this.resource = resource;
	}

	public String getResourceName()
	{
		return resourceName;
	}

	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	public void setFeatureTypeLink(String featureTypeLink)
	{
		this.featureTypeLink = featureTypeLink;
	}

	public String getFeatureTypeLink()
	{
		return featureTypeLink;
	}

	public void setCoveragestore(String coveragestore)
	{
		this.coveragestore = coveragestore;
	}

	public String getCoveragestore()
	{
		return coveragestore;
	}

	@Override
	public String toString() {
		return "GeosereverLayer [id=" + id + ", type=" + type + ", defaultStyle=" + defaultStyle + ", styles=" + styles
				+ ", enabled=" + enabled + ", resource=" + resource + ", resourceName=" + resourceName
				+ ", featureTypeLink=" + featureTypeLink + ", datastore=" + datastore + ", workspace=" + workspace
				+ ", coveragestore=" + coveragestore + ", title=" + title + "]";
	}
	
	
}
