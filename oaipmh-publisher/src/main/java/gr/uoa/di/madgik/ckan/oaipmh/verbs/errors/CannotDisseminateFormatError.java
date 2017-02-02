package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public class CannotDisseminateFormatError extends ErrorCondition {

	private static final long serialVersionUID = 1L;
	private final String code = "cannotDisseminateFormat";

	public String getMessage() {
		return null;
	}

	public String getCode() {
		return code ;
	}

}
