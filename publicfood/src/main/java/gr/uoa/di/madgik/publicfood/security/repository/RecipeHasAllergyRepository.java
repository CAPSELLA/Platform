package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.AllergyEntity;
import gr.uoa.di.madgik.publicfood.model.RecipeHasAllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeHasAllergyRepository extends JpaRepository<RecipeHasAllergyEntity, Long> {

    List<RecipeHasAllergyEntity> findByRecipeIdAndRecipeDomainName(int recipeId, String domain);

}