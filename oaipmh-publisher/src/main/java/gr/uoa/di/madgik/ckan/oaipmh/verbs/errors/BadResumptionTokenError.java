package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public class BadResumptionTokenError extends ErrorCondition {

	private static final long serialVersionUID = 1L;
	private final String code = "badResumptionToken";
    private String message = null;

    public BadResumptionTokenError() {
    }

    public BadResumptionTokenError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}
