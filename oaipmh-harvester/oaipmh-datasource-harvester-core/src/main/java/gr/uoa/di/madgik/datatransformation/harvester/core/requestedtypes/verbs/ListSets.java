package gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs;

public class ListSets {

	private final static String verb = "ListSets"; 
	private String resumptionToken;

	private ListSets() {}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}
	
	public static String getVerb() {
		return verb;
	}

	public static class ListSetsBuilder {
		private String resumptionToken;
		
		public ListSetsBuilder() {}
		
		public ListSetsBuilder resumptionToken(String resumptionToken) {
			this.resumptionToken = resumptionToken;
			return this;
		}
		
		public ListSets build() {
			ListSets listSets = new ListSets();
			listSets.setResumptionToken(this.resumptionToken);
			return listSets;
		}
	}

}