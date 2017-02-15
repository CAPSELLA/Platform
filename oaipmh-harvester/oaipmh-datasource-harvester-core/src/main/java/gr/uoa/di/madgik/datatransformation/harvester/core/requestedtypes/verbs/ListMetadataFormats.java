package gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs;


public class ListMetadataFormats {

	private final static String verb = "ListMetadataFormats"; 
	private String identifier;
	
	private ListMetadataFormats() {}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public static String getVerb() {
		return verb;
	}

	public static class ListMetadataFormatsBuilder {
		private String identifier;
		
		public ListMetadataFormatsBuilder() {}
		
		public ListMetadataFormatsBuilder identifier(String identifier) {
			this.identifier = identifier;
			return this;
		}
		
		public ListMetadataFormats build() {
			ListMetadataFormats listMetadataFormats = new ListMetadataFormats();
			listMetadataFormats.setIdentifier(this.identifier);
			return listMetadataFormats;
		}
	}
}