package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.AllergyEntity;
import gr.uoa.di.madgik.publicfood.model.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {

    List<AreaEntity> findByDomainName(String domain_name);
    AreaEntity findByDomainNameAndIdarea(String domain_name, int idArea);

}
