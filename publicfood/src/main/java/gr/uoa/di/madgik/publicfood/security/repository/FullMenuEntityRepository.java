package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.DomainEntity;
import gr.uoa.di.madgik.publicfood.model.FullMenuEntity;
import gr.uoa.di.madgik.publicfood.model.MenuMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FullMenuEntityRepository extends JpaRepository<FullMenuEntity, Long> {

    FullMenuEntity findByDomainAndActive(String domain, int active);
    FullMenuEntity findByDomainAndId(String domain, int id);
}