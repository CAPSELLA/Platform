package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.PostCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PostCodeRepository extends JpaRepository<PostCodeEntity, Long> {

    List<PostCodeEntity> findByDomainName(String domain_name);
}
