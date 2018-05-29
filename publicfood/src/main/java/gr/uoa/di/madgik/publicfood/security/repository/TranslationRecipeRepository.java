package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.TranslationRecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRecipeRepository extends JpaRepository<TranslationRecipeEntity, Long> {


    TranslationRecipeEntity findByRecipeId1AndLanguageAndRecipeDomainName1(int id, String language, String domain);
}