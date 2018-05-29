package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.TranslationRecipeEntity;
import gr.uoa.di.madgik.publicfood.model.UserHasChildrenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserHasChildrenRepository extends JpaRepository<UserHasChildrenEntity, Long> {


    List<UserHasChildrenEntity> findByUserIdAndDomainName(long id, String domain);
    UserHasChildrenEntity findByChildrenIdchildrenAndDomainName(int childrenIdchildren, String domain);

}