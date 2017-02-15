package gr.uoa.di.madgik.datatransformation.harvester.core.utils.retrieveinfo;

public class AdditionalInfoForRepo {
	
	String granularity = null;
	String repositoryName = null;
	String earliestDatestamp = null;
	
	public String getGranularity() {
		return granularity;
	}
	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getEarliestDatestamp() {
		return earliestDatestamp;
	}
	public void setEarliestDatestamp(String earliestDatestamp) {
		this.earliestDatestamp = earliestDatestamp;
	}
	
}