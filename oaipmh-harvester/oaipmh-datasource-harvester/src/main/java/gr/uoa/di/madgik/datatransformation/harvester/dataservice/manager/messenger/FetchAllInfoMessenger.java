package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger;

import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.DefaultTime;

public class FetchAllInfoMessenger {

	private List<InfoForPopulation> info = new ArrayList<InfoForPopulation>();
	DefaultTime dt = null;
	
	public List<InfoForPopulation> getInfo() {
		return info;
	}
	public void setInfo(List<InfoForPopulation> info) {
		this.info = info;
	}
	
	public DefaultTime getDt() {
		return dt;
	}
	public void setDt(DefaultTime dt) {
		this.dt = dt;
	}
	
}
