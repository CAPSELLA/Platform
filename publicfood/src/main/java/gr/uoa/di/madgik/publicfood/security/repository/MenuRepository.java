package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findByFullMenuId(int fullMenuId);

}