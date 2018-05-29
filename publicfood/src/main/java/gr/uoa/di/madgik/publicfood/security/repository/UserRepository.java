package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByProviderUuidAndProvideridAndDomainName(String providerUuid, int providerid, String Domain);
    List<UserEntity> findByDomainName(String Domain);
    List<UserEntity> findByDomainNameAndStatus(String Domain, int status);
    UserEntity findByIdAndDomainName(long id, String Domain);


    @Query("SELECT max(u.id) FROM UserEntity u")
    Integer getLastID();

}
