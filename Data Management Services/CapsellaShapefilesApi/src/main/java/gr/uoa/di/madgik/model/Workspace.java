package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workspace implements Serializable
{
	private static final long serialVersionUID = 8604630097667207913L;

	private String name = "";
	private Map<String, ArrayList<String>> stores = new HashMap<String, ArrayList<String>>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<String> getStores(String typeStore)
	{
		return stores.get(typeStore);
	}

	public void setStores(String typeStore, List<String> list)
	{
		this.stores.put(typeStore, (ArrayList<String>) list);
	}

	@Override
	public String toString() {
		return "Workspace [name=" + name + ", stores=" + stores + "]";
	}
	
}
