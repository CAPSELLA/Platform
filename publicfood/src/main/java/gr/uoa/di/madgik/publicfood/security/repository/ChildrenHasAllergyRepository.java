package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.AllergyEntity;
import gr.uoa.di.madgik.publicfood.model.ChildrenHasAllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ChildrenHasAllergyRepository extends JpaRepository<ChildrenHasAllergyEntity, Long> {

    List<ChildrenHasAllergyEntity> findByChildrenIdchildrenAndDomainName(int childId, String domain_name);


}
