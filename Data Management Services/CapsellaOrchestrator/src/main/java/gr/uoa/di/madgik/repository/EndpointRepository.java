package gr.uoa.di.madgik.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.Endpoint;

@Repository
public interface EndpointRepository extends CrudRepository<Endpoint, String>{
	
	public List<Endpoint> findEndpointUrlByMetadataUuid(String uuid);

}
