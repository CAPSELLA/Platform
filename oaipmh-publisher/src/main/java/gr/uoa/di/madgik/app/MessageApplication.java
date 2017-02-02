package gr.uoa.di.madgik.app;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

import gr.uoa.di.madgik.rest.OAIPMHRest;

public class MessageApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public MessageApplication() {
		singletons.add(new OAIPMHRest());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}