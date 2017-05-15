package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LayerGroup implements Serializable
{
	private static final long serialVersionUID = 7020179483138031362L;

	private String name = null;
	private ArrayList<String> layers = null;
	private Map<String, String> styles = new HashMap<String, String>();
	private Bounds bounds = null;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<String> getLayers()
	{
		return layers;
	}

	public void setLayers(ArrayList<String> layers)
	{
		this.layers = layers;
	}

	public Collection<String> getStyles()
	{

		ArrayList<String> result = new ArrayList<String>();
		for (String l : this.layers)
		{
			result.add(this.getStyle(l));
		}

		return result;
	}

	public Bounds getBounds()
	{
		return bounds;
	}

	public void setBounds(Bounds bounds)
	{
		this.bounds = bounds;
	}

	public void addLayer(String layer)
	{
		this.layers.add(layer);
	}

	public String getStyle(String layer)
	{
		return this.styles.get(layer);
	}

	public void addStyle(String layer, String style)
	{
		this.styles.put(layer, style);
	}

	public void setStyles(Map<String, String> styles)
	{
		this.styles = styles;
	}

	@Override
	public String toString() {
		return "LayerGroup [name=" + name + ", layers=" + layers + ", styles=" + styles + ", bounds=" + bounds + "]";
	}
}
