package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeatureType implements Serializable
{
	public enum ProjectionPolicy 
	{
		REPROJECT_TO_DECLARED, FORCE_DECLARED, NONE
	}
	
	private static final long serialVersionUID = -4822555908243595435L;

	private String name = null;
	private String nativeName = null;
	private String title = null;
	private String nativeCRS = null;
	private String srs = null;

	private Bounds nativeBoundingBox;
	private Bounds latLonBoundingBox;

	private ProjectionPolicy projectionPolicy = ProjectionPolicy.FORCE_DECLARED;
	private int maxFeatures = 0;
	private int numDecimals = 0;
	private boolean enabled = false;

	private Map<String, String> metadata = new HashMap<String, String>();
	private String workspace = null;
	private String datastore = null;

	public String getWorkspace()
	{
		return workspace;
	}

	public void setWorkspace(String workspace)
	{
		this.workspace = workspace;
	}

	public String getDatastore()
	{
		return datastore;
	}

	public void setDatastore(String datastore)
	{
		this.datastore = datastore;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNativeName()
	{
		return nativeName;
	}

	public void setNativeName(String nativeName)
	{
		this.nativeName = nativeName;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getNativeCRS()
	{
		return nativeCRS;
	}

	public void setNativeCRS(String nativeCRS)
	{
		this.nativeCRS = nativeCRS;
	}

	public String getSrs()
	{
		return srs;
	}

	public void setSrs(String srs)
	{
		this.srs = srs;
	}

	public Bounds getNativeBoundingBox()
	{
		return nativeBoundingBox;
	}

	public void setNativeBoundingBox(Bounds nativeBoundingBox)
	{
		this.nativeBoundingBox = nativeBoundingBox;
	}

	public Bounds getLatLonBoundingBox()
	{
		return latLonBoundingBox;
	}

	public void setLatLonBoundingBox(Bounds latLonBoundingBox)
	{
		this.latLonBoundingBox = latLonBoundingBox;
	}

	public ProjectionPolicy getProjectionPolicy()
	{
		return projectionPolicy;
	}

	public void setProjectionPolicy(ProjectionPolicy projectionPolicy)
	{
		this.projectionPolicy = projectionPolicy;
	}

	public int getMaxFeatures()
	{
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures)
	{
		this.maxFeatures = maxFeatures;
	}

	public int getNumDecimals()
	{
		return numDecimals;
	}

	public void setNumDecimals(int i)
	{
		this.numDecimals = i;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public Map<String, String> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata)
	{
		this.metadata = metadata;
	}

	public void setMetadata(String key, String value)
	{
		this.metadata.put(key, value);
	}

	@Override
	public String toString() {
		return "FeatureType [name=" + name + ", nativeName=" + nativeName + ", title=" + title + ", nativeCRS="
				+ nativeCRS + ", srs=" + srs + ", nativeBoundingBox=" + nativeBoundingBox + ", latLonBoundingBox="
				+ latLonBoundingBox + ", projectionPolicy=" + projectionPolicy + ", maxFeatures=" + maxFeatures
				+ ", numDecimals=" + numDecimals + ", enabled=" + enabled + ", metadata=" + metadata + ", workspace="
				+ workspace + ", datastore=" + datastore + "]";
	}
	
	
}
