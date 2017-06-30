package gr.uoa.di.madgik.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.UserToken;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

	UserToken findByToken(String token);
}
