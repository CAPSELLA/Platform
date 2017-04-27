package gr.uoa.di.madgik.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import gr.uoa.di.madgik.entities.JsonEntities;



public interface CustomerRepository extends MongoRepository<JsonEntities, String> {

	
}