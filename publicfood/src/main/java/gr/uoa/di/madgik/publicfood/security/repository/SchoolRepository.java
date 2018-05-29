package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.PostCodeEntity;
import gr.uoa.di.madgik.publicfood.model.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(exported = false)
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {

    List<SchoolEntity> findByDomainName(String domain_name);
    List<SchoolEntity> findByDomainNameAndPostCodeIdPostCode(String domain_name, int postCode);
    List<SchoolEntity> findByDomainNameAndCookingCenterId(String domain_name, int cookingCenterId);

    SchoolEntity findByIdAndDomainName(int id, String domain_name);

}
