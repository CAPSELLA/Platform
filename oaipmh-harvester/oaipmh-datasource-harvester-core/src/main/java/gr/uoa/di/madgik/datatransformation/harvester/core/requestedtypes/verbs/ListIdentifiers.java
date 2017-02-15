package gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs;


public class ListIdentifiers {

	private final static String verb = "ListIdentifiers"; 
	private String metadataPrefix;
	
	private String from;
	private String until;
	private String set;
	private String resumptionToken;
	
	private ListIdentifiers() {}
	
	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getUntil() {
		return until;
	}
	public void setUntil(String until) {
		this.until = until;
	}
	
	public String getSet() {
		return set;
	}
	public void setSet(String set) {
		this.set = set;
	}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public static String getVerb() {
		return verb;
	}

	public static class ListIdentifiersBuilder {
		private String metadataPrefix;
		private String from;
		private String until;
		private String set;
		private String resumptionToken;
		
		public ListIdentifiersBuilder(String metadataPrefix) {
			this.metadataPrefix = metadataPrefix;
		}
		
		public ListIdentifiersBuilder from(String from) {
			this.from = from;
			return this;
		}

		public ListIdentifiersBuilder until(String until) {
			this.until = until;
			return this;
		}

		public ListIdentifiersBuilder set(String set) {
			this.set = set;
			return this;
		}
		
		public ListIdentifiersBuilder resumptionToken(String resumptionToken) {
			this.resumptionToken = resumptionToken;
			return this;
		}
		
		public ListIdentifiers build() {
			ListIdentifiers listIdentifiers = new ListIdentifiers();
			listIdentifiers.setFrom(this.from);
			listIdentifiers.setMetadataPrefix(this.metadataPrefix);
			listIdentifiers.setResumptionToken(this.resumptionToken);
			listIdentifiers.setSet(this.set);
			listIdentifiers.setUntil(this.until);
			return listIdentifiers;
		}
	}
	
}