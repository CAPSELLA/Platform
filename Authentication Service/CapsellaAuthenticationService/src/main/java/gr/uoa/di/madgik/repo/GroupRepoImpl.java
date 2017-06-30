package gr.uoa.di.madgik.repo;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;

import gr.uoa.di.madgik.model.Group;
import gr.uoa.di.madgik.model.User;

public class GroupRepoImpl implements GroupRepoExtension, BaseLdapNameAware {
	
	@Value("${ldap.adminUser}")
	private String ADMIN_USER; 

	@Autowired
	GroupRepo groupRepo;
	private final LdapTemplate ldapTemplate;
	@Value("${ldap.contextSource.base}")
	private LdapName baseLdapPath;
	
	@Value("${ldap.GroupOU}")
	private String groupOU;
	
	@Value("${ldap.UserOU}")
	private String userOU;
	
	@Value("${ldap.GroupSearchBase}")
	private String groupSearchBase;

	@Autowired
	public GroupRepoImpl(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Override
	public void setBaseLdapPath(LdapName baseLdapPath) {
		this.baseLdapPath = baseLdapPath;
	}

	private Name buildGroupDn(String groupName) {
		return LdapNameBuilder.newInstance().add(groupOU).add("cn", groupName).build();
	}

	private Name buildUserDn(String username) {
		return LdapNameBuilder.newInstance(baseLdapPath).add(userOU).add("uid", username).build();
	}

	@Override
	public List<String> getAllGroupNames() {
		LdapQuery query = query().attributes("cn").where("objectclass").is(groupSearchBase);

		return ldapTemplate.search(query, new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attributes) throws NamingException {
				return (String) attributes.get("cn").get();
			}
		});
	}
	
	public List<String> getUserGroups(User user) {
		
		Name userDn = buildUserDn(user.getUsername());
		LdapQuery query = query().attributes("cn").where("objectclass").is(groupSearchBase).and(query().where("memberUid").is(userDn.toString()));

		return ldapTemplate.search(query, new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attributes) throws NamingException {
				return (String) attributes.get("cn").get();
			}
		});
	}
	
	
	public List<String> getUserGroupsByUsername(String userrname) {
		
		Name userDn = buildUserDn(userrname);
		LdapQuery query = query().attributes("cn").where("objectclass").is(groupSearchBase).and(query().where("memberUid").is(userDn.toString()));

		return ldapTemplate.search(query, new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attributes) throws NamingException {
				return (String) attributes.get("cn").get();
			}
		});
	}
	
	

	@Override
	public void create(Group group) {
		group.addMember(LdapUtils.prepend(LdapUtils.newLdapName(ADMIN_USER), baseLdapPath));
		ldapTemplate.create(group);
	}

	public void addMemberToGroup(String groupName, User user) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildUserDn(user.getUidNumber());

		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.addAttributeValue("uniqueMember", userDn);
		ldapTemplate.modifyAttributes(ctx);
	}

	public void removeMemberFromGroup(String groupName, User user) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildUserDn(user.getUidNumber());

		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.removeAttributeValue("uniqueMember", userDn);
		ldapTemplate.modifyAttributes(ctx);
	}

	public boolean isMemberInGroup(String groupName, User user) {
		Name userDn = buildUserDn(user.getUsername());
		LdapQuery query = query().attributes("cn").where("objectclass").is(groupSearchBase)
				.and(query().where("cn").is(groupName).and(query().where("memberUid").is(userDn.toString())));

		List<String> persons = ldapTemplate.search(query, new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attributes) throws NamingException {
				return (String) attributes.get("cn").get();
			}
		});

		if (persons.size() > 0)
			return true;
		else
			return false;
	}
}
