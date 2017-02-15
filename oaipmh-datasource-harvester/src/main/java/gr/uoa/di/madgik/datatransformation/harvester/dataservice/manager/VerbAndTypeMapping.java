package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager;

import java.util.HashMap;

public class VerbAndTypeMapping {

	private HashMap<String, String> mapVerbMetPref = new HashMap<>();
	private TypeOfPeriodicity hasDefaultPeriodic = TypeOfPeriodicity.DEFAULT;
	
	public HashMap<String, String> getMapVerbMetPref() {
		return mapVerbMetPref;
	}
	public void setMapVerbMetPref(HashMap<String, String> mapVerbMetPref) {
		this.mapVerbMetPref = mapVerbMetPref;
	}
	
	public TypeOfPeriodicity getHasDefaultPeriodic() {
		return hasDefaultPeriodic;
	}
	public void setHasDefaultPeriodic(TypeOfPeriodicity hasDefaultPeriodic) {
		this.hasDefaultPeriodic = hasDefaultPeriodic;
	}
	
}