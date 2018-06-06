package gr.uoa.di.madgik.model;

import java.util.HashMap;
import java.util.Map;

public class Record {

	private Map<String, String> values;

	public Record() {
		this.values = new HashMap<String, String>();
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	public void put(String key, String value) {
		values.put(key, value);
	}

	public void get(String key) {
		values.get(key);
	}
}
