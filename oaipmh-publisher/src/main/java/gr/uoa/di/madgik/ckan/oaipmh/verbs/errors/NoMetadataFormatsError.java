package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public class NoMetadataFormatsError extends ErrorCondition {

	private static final long serialVersionUID = 1L;
	private final String code = "NoMetadataFormatsError";
	
	public String getMessage() {
		return null;
	}

	public String getCode() {
		return code;
	}

}
