package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager;

import java.util.HashMap;
import java.util.List;

public class ReposInfoMessenger {

	HashMap<String, List<HashMap<String, String>>> info = new HashMap<>();

	public HashMap<String, List<HashMap<String, String>>> getInfo() {
		return info;
	}
	public void putInfo(String key, List<HashMap<String, String>> info) {
		this.info.put(key, info);
	}
}
