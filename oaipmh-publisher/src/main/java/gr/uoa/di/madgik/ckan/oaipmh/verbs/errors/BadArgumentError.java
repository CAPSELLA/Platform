package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public class BadArgumentError extends ErrorCondition {
	
	private static final long serialVersionUID = 1L;
	private final String code = "badArgument";
    private String message = null;

    public BadArgumentError() {
    }

    
    public BadArgumentError(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

}
