package gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs;

public class GetRecord {

	/* required */
	private final static String verb = "GetRecord"; 
	
	private String identifier;
	private  String metadataPrefix;
	
	private GetRecord() {}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getMetadataPrefix() {
		return metadataPrefix;
	}
	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}
	
	public static String getVerb() {
		return verb;
	}
	
	public static class GetRecordBuilder {
		private String identifier;
		private String metadataPrefix;
		
		public GetRecordBuilder(String identifier, String metadataPrefix) {
			this.identifier = identifier;
			this.metadataPrefix = metadataPrefix;
		}
		
		public GetRecord build() {
			GetRecord getRecord = new GetRecord();
			getRecord.setIdentifier(this.identifier);
			getRecord.setMetadataPrefix(this.metadataPrefix);
			return getRecord;
		}
	}
	
}