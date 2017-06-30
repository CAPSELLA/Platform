package gr.uoa.di.madgik.ckan.core;

public class HarvestedInfoObject {
	
	private String uri;
	RetrievedNodes retrievedNodes = null;
	private String metadataPrefix = null;
	private String verb = null;
	/*hash of current uri*/
	private String collectionID = null;
	private Boolean firstTimeToStore = true;
	
	public HarvestedInfoObject() {}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public RetrievedNodes getRetrievedNodes() {
		return retrievedNodes;
	}

	public void setRetrievedNodes(RetrievedNodes retrievedNodes) {
		this.retrievedNodes = retrievedNodes;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public void setCollectionID(String collectionID){
		this.collectionID = collectionID;
	}
	public String getCollectionID(){
		return collectionID;
	}

	public Boolean getFirstTimeToStore() {
		return firstTimeToStore;
	}
	public void setFirstTimeToStore(Boolean firstTimeToStore) {
		this.firstTimeToStore = firstTimeToStore;
	}

}
