package gr.uoa.di.madgik.datatransformation.harvester.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RequestData {

	private final static Logger logger = Logger.getLogger(RequestData.class);

	public static DataPublisherResponse requestHarvest(String url, Map<String, String> parameters) throws Exception {
		DataPublisherResponse dataPublisherResponse = new DataPublisherResponse();
			
		Client client = Client.create();
		WebResource webResource = null;
		MultivaluedMap<String, String> nameValuePairs = new MultivaluedMapImpl();
		for (Map.Entry<String, String> params: parameters.entrySet()) {
			if (params.getKey()!=null && !params.getKey().isEmpty() && 
				params.getValue()!=null && !params.getValue().isEmpty())
				nameValuePairs.add(params.getKey(), params.getValue());
		}
		webResource = client.resource(url).queryParams(nameValuePairs);
		ClientResponse response = null;
		try {
			response = webResource.get(ClientResponse.class);
		} catch (Exception e) {
			dataPublisherResponse.setErrorCode(-2);
			return dataPublisherResponse;
		}

		if (response.getStatus()!=201 && response.getStatus()!=200) {
			logger.info("Failed : HTTP error code : " + response.getStatus());
			if (response.getStatus()==301) {
				List<String> locations;
				List<String> locationsReturned = new ArrayList<String>();
				if ((locations = response.getHeaders().get("Location"))!=null && !locations.isEmpty()) {
					for (String loc: locations)
						locationsReturned.add(loc.split("\\?")[0]);
				}
				return new DataPublisherResponse(locationsReturned, 301, "Redirect to another data publisher");
			}
			
			if (response.getStatus()==500) {
				dataPublisherResponse.setErrorCode(500);
				dataPublisherResponse.setErrorMessage("The server encountered an unexpected condition which prevented it from fulfilling the request");
			} else if (response.getStatus()==503) {
				int retryAfter;
				if (response.getHeaders().get("Retry-After")!=null &&
						(retryAfter=Integer.parseInt(response.getHeaders().get("Retry-After").get(0)))!=0) {
					dataPublisherResponse.setRetryAfter(retryAfter);
				}
				dataPublisherResponse.setErrorCode(503);
				dataPublisherResponse.setErrorMessage("The server will be alleviated after some delay.");
				logger.error(response.toString());
			} else {
				dataPublisherResponse.setErrorCode(response.getStatus());
				dataPublisherResponse.setErrorMessage(response.toString());
			}
		}
		dataPublisherResponse.setBody(response.getEntity(String.class));
		return dataPublisherResponse;
	}
	
}
