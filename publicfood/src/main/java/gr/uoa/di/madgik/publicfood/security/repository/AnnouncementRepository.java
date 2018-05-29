package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

    AnnouncementEntity findByDomainAndId(String domain, int id);

    AnnouncementEntity findById(int id);

    List<AnnouncementEntity> findByDomain(String domain);

    @Query("SELECT max(u.id) FROM AnnouncementEntity u")
    Integer getLastID();
}
