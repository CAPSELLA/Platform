package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.TranslationAllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(exported = false)
public interface TranslationAllergyRepository  extends JpaRepository<TranslationAllergyEntity, Long> {


   List<TranslationAllergyEntity> findByAllergyDomainNameAndLanguage(String Domain, String language);
   TranslationAllergyEntity findByAllergyDomainNameAndLanguageAndAllergyIdallergy(String Domain, String language, int allergyId);

}