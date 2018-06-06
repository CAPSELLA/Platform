package gr.uoa.di.madgik.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gr.uoa.di.madgik.model.DatasetStatus;
import gr.uoa.di.madgik.model.Endpoint;
import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.model.EndpointType;
import gr.uoa.di.madgik.repository.MetadataRepository;
import gr.uoa.di.madgik.repository.EndpointRepository;;


@Service
public class MetadataService {
	
	@Resource
	MetadataRepository repository;
	
	@Resource
	EndpointRepository endpointRepository;
	
	@Autowired
	public MetadataService(){
		
	}
	
	
	public Metadata insertMetadata (Metadata metadata){
		
	//	metadata= repository.save(metadata);
	   
		
		return repository.save(metadata);
		
			
	}
	
	public String getContentType(String uuid ){
		
		return repository.findContent_typeByUuid(uuid);
	}
	
	public Metadata findByUUID(String uuid){
		return repository.findByUuid(uuid);

	}
	
	public List<Metadata> findByDatasetGroup(String datasetGroup){
		return repository.findByDatasetGroup(datasetGroup);

	}
	
	public List<Metadata> findByUsername(String username, String status ){
		return repository.findByUsernameAndStatus(username, status);

	}
	
	public List<Metadata> findByGroup(String group, String status ){
		return repository.findByOwnerGroupAndStatus(group, status);

	}
	
	public List<Metadata> findByAccess(String access, String status ){
		return repository.findByAccessAndStatus(access, status);

	}
	
	public List<Metadata> findByDate(Timestamp date ){
		return repository.findNewOrUpdatedDatasets(date);

	}
	
	
	public void deleteByUUID(String uuid ){
		 repository.delete(uuid);

	}
	
  
	public List<Metadata> findDatasetsFordelete(Timestamp date) throws InterruptedException {
    	
	  return (repository.findDatasetsForDelete(date));
	}
	
	public Endpoint insertEndpoint(Endpoint endpoint){
		return endpointRepository.save(endpoint);
	}
	
	public List<Endpoint> findEndpointsByUuid(String uuid){
	//	List<String> urls = new ArrayList();
		List<Endpoint> endpoints = endpointRepository.findEndpointUrlByMetadataUuid(uuid);
		for(Endpoint endpoint : endpoints)
		{
			endpoint.setMetadata(findByUUID(endpoint.getMetadata().getUuid()));
		}
		
//		for(Endpoint endpoint: endpoints ){
//			urls.add(endpoint.getEndpointUrl());
//		}
		return endpoints;
	}
	
	public Endpoint insertEndpoint(Metadata metadata, String url, String type){
		Endpoint endpoint = new Endpoint();
		endpoint.setEndpointUrl(url);
		endpoint.setType(type);
		endpoint.setMetadata(metadata);
		
		endpoint = insertEndpoint(endpoint);
		
		return endpoint;
	}
	
	
}
