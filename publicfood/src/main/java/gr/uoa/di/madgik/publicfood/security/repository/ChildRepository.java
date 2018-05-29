package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.ChildrenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(exported = false)
public interface ChildRepository extends JpaRepository<ChildrenEntity, Integer> {

    @Query("SELECT max(c.idchildren) FROM ChildrenEntity c")
    Integer getLastID();
    ChildrenEntity findByIdchildrenAndDomainName(int id, String domain);
    List<ChildrenEntity> findByDomainNameAndSchoolId(String domain, int schoolId);

}
