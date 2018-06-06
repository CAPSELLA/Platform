package gr.uoa.di.madgik.repo;

import java.util.Collection;

import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.repository.LdapRepository;
import org.springframework.ldap.repository.Query;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

import gr.uoa.di.madgik.model.Group;

@Repository
public interface GroupRepo extends LdapRepository<Group>, GroupRepoExtension {

	public final static String ADMIN_GROUP_STRING = "admin";
	public final static String User_READ_WRITE_GROUP_STRING = "users";
	public final static String User_READ_GROUP_STRING = "userR";
	public final static String ADMIN = "ADMIN";
	public static final String USER = "USER";

	Group findByName(String groupName);
	
	Group findByFullName(String fullName);


	@Query("(member={0})")
	Collection<Group> findByMember(Name member);
}
