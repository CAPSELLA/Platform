package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.SchoolHasFullMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface SchoolHasFullMenuRepository extends JpaRepository<SchoolHasFullMenuEntity, Long> {

    SchoolHasFullMenuEntity findBySchoolIdAndSchoolDomainName(int schoolId, String domain_name);
}