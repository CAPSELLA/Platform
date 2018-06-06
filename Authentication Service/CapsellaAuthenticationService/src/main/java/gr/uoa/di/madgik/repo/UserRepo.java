package gr.uoa.di.madgik.repo;

import java.util.List;

import org.springframework.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.*;

@Repository
public interface UserRepo extends LdapRepository<User> {

	List<User> findByFullNameContains(String name);
	
	User findOneByUsername(String username);

	User findOneByUsernameAndUserPassword(String username, String userPassword);
}