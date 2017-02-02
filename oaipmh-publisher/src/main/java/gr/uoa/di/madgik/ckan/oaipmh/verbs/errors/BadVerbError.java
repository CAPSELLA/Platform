package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public class BadVerbError extends ErrorCondition {

	private static final long serialVersionUID = 1L;
	private final String code = "badVerb";
    private String message = null;

    public BadVerbError() {

    }

    public BadVerbError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}
