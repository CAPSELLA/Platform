package gr.uoa.di.madgik.ckan.oaipmh.verbs.errors;

public abstract class ErrorCondition extends Exception {

	private static final long serialVersionUID = 1L;

	abstract public String getMessage();

	abstract public String getCode();
}
