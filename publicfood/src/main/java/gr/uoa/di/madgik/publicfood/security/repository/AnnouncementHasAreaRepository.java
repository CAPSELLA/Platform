package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.AnnouncementHasAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface AnnouncementHasAreaRepository extends JpaRepository<AnnouncementHasAreaEntity, Long> {

    List<AnnouncementHasAreaEntity> findByDomainAndAreaId(String domain, int id);
    List<AnnouncementHasAreaEntity> findByDomainAndAnnouncementId(String domain, int id);

    @Query("SELECT max(u.id) FROM AnnouncementHasAreaEntity u")
    Integer getLastID();
}
