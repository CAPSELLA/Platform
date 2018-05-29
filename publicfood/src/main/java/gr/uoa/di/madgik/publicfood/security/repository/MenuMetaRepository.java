package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.DomainEntity;
import gr.uoa.di.madgik.publicfood.model.MenuMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MenuMetaRepository extends JpaRepository<MenuMetaEntity, Long> {

    MenuMetaEntity findByMenuDomainNameAndMenuId(String domain, int menuId);

}
