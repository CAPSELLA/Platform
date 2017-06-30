package gr.uoa.di.madgik.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.Metadata;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, String>{
	
	
	String findContent_typeByUuid(String uuid);
	
	Metadata findByUuid(String uuid);
	
	List<Metadata> findByUsernameAndStatus(String username, String status);
	
	List<Metadata> findByOwnerGroupAndStatus(String owner_group, String status);
	
	List<Metadata> findByAccessAndStatus(String access, String status);
	
	@Query("SELECT m FROM Metadata m where m.lastUpdated > ?1 and ( m.status=\'active\' or m.status=\'inactive\')") 
	List<Metadata> findNewOrUpdatedDatasets(@Param("date") Timestamp date);
	
	@Query("SELECT m FROM Metadata m where m.lastUpdated <= ?1 and m.status=\'inactive\'") 
	List<Metadata> findDatasetsForDelete(@Param("date") Timestamp date);
}
