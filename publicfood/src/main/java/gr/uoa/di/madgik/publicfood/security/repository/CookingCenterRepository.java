package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.CookingCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface CookingCenterRepository extends JpaRepository<CookingCenterEntity, Long> {

    List<CookingCenterEntity> findByDomainName(String domain);
    List<CookingCenterEntity> findByDomainNameAndArea(String domain, int area);
    CookingCenterEntity findByDomainNameAndId(String domain, int id);

}
