package gr.uoa.di.madgik.publicfood.security.repository;


import gr.uoa.di.madgik.publicfood.model.AllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {

    List<AllergyEntity> findByDomainName(String domain_name);
    AllergyEntity findByIdallergyAndDomainName(int idAllergy, String domain);


}

