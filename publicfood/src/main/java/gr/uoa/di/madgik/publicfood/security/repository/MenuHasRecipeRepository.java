package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.MenuHasRecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuHasRecipeRepository extends JpaRepository<MenuHasRecipeEntity, Long> {

    List<MenuHasRecipeEntity> findByMenuIdAndDomainName(int menuId, String domain);

}