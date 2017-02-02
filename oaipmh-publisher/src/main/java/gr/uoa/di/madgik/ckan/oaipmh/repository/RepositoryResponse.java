package gr.uoa.di.madgik.ckan.oaipmh.repository;

import javax.ws.rs.core.UriInfo;

import gr.uoa.di.madgik.ckan.oaipmh.verbs.Verb;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.VerbFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author  
 *
 */
public class RepositoryResponse {

    private RepositoryResponse(UriInfo request) {

    }

    public static RepositoryResponseBuilder request(UriInfo request) {
        return new RepositoryResponseBuilder(request);
    }

    public static class RepositoryResponseBuilder {

        private UriInfo request;

        private String response;

        private RepositoryResponseBuilder(UriInfo request) {
            this.request = request;
        }

        public RepositoryResponseBuilder response(Repository repository) {
            Logger.getLogger(RepositoryResponseBuilder.class.getName()).log(Level.DEBUG, "response");

            response = request.getAbsolutePath().toString();

            Verb verb = VerbFactory.getVerbFactoryMethod(request.getQueryParameters().getFirst("verb"));
            verb.setAttributes(request);

            this.response = verb.response(repository);

            return this;
        }

        public String build() {
            return response;
        }
    }
}
