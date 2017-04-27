package gr.uoa.di.madgik.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gr.uoa.di.madgik.model.Metadata;
import gr.uoa.di.madgik.repository.MetadataRepository;

@Service
public class MetadataService {
	
	@Resource
	MetadataRepository repository;
	
	
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
	
	public Metadata findByUUID(String uuid ){
		return repository.findByUuid(uuid);

	}
	
	public List<Metadata> findByUsername(String username ){
		return repository.findByUsername(username);

	}
	
	public List<Metadata> findByGroup(String group ){
		return repository.findByOwnerGroup(group);

	}
	
	public List<Metadata> findByAccess(String access ){
		return repository.findByAccess(access);

	}
	
	public void deleteByUUID(String uuid ){
		 repository.delete(uuid);

	}
	
	
}
