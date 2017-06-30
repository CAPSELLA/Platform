package gr.uoa.di.madgik.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group,String>{

	Group findByName(String name);
	
	
}
