package gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs;

public class ListRecords {

	/* required */
	private final static String verb = "ListRecords";
	private String metadataPrefix;
	
	/* optional */
	private String from;
	private String until;
	private String set;
	private String resumptionToken;
	
	private ListRecords() {}
	
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

	public static class ListRecordsBuilder {
		private String metadataPrefix;
		private String from;
		private String until;
		private String set;
		private String resumptionToken;
		
		public ListRecordsBuilder(String metadataPrefix) {
			this.metadataPrefix = metadataPrefix;
		}
		
		public ListRecordsBuilder from(String from) {
			this.from = from;
			return this;
		}

		public ListRecordsBuilder until(String until) {
			this.until = until;
			return this;
		}

		public ListRecordsBuilder set(String set) {
			this.set = set;
			return this;
		}
		
		public ListRecordsBuilder resumptionToken(String resumptionToken) {
			this.resumptionToken = resumptionToken;
			return this;
		}
		
		public ListRecords build() {
			ListRecords listRecords = new ListRecords();
			listRecords.setMetadataPrefix(this.metadataPrefix);
			listRecords.setFrom(this.from);
			listRecords.setUntil(this.until);
			listRecords.setSet(this.set);
			listRecords.setResumptionToken(this.resumptionToken);
			return listRecords;
		}
		
	}
	
}