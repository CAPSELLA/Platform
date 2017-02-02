package gr.uoa.di.madgik.rest;

import gr.uoa.di.madgik.CKANAdapterAPI;
import gr.uoa.di.madgik.CKANRepository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.RepositoryResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author  
 */
@Path("")
public class OAIPMHRest {

	Logger logger = Logger.getLogger(OAIPMHRest.class.getName());

	CKANRepository repository;

	public OAIPMHRest() {
		logger.debug("Create OAIPMHResource");
		InputStream stream = null;
		try {
			stream = CKANRepository.class.getClassLoader().getResourceAsStream("ckan.properties");
		} catch (Exception e) {
			logger.fatal("cannot find ckan.properties file");
		}
		Properties props = new Properties();
		try {
			props.load(stream);
		} catch (IOException ex) {
			logger.error(null, ex);
		}

		String ckan_url = props.getProperty("url").trim();
		String apiKey = props.getProperty("apiKey").trim();
		int limit = Integer.parseInt(props.getProperty("limit").trim());

		CKANAdapterAPI ckanAdapter = new CKANAdapterAPI(ckan_url, apiKey, limit);
		ckanAdapter.createClient();
		repository = new CKANRepository(ckanAdapter);
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public Response query(@Context UriInfo request) {
		logger.debug("oai publisher was called");

		String response = RepositoryResponse.request(request).response(repository).build();

		return Response.ok(response).build();
	}
}
