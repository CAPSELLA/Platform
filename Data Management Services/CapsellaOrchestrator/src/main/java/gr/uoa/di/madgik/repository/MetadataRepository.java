package gr.uoa.di.madgik.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.Metadata;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, String>{
	
	
	String findContent_typeByUuid(String uuid);
	
	Metadata findByUuid(String uuid);
	
	List<Metadata> findByUsername(String username);
	
	List<Metadata> findByOwnerGroup(String owner_group);
	
	List<Metadata> findByAccess(String access);
}
