package gr.uoa.di.madgik.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import gr.uoa.di.madgik.model.Group;
import gr.uoa.di.madgik.model.User;
import gr.uoa.di.madgik.repo.GroupRepo;
import gr.uoa.di.madgik.repo.GroupRepoImpl;
import gr.uoa.di.madgik.repo.UserRepo;

@Service
@Component
public class UserService implements BaseLdapNameAware {
	private final UserRepo userRepo;
	private final GroupRepo groupRepo;
	private final GroupRepoImpl groupRepoImpl;
	@Value("${ldap.contextSource.base}")
	private LdapName basePath;

	@Value("${ldap.userGroup}")
	private String USER_GROUP;

	@Value("${ldap.adminGroup}")
	private String ADMIN_GROUP;

	@Autowired
	public UserService(UserRepo userRepo, GroupRepo groupRepo, GroupRepoImpl groupRepoImpl) {
		this.userRepo = userRepo;
		this.groupRepoImpl = groupRepoImpl;
		this.groupRepo = groupRepo;
	}

	public Group getUserGroup() {
		return groupRepo.findOne(LdapUtils.newLdapName(USER_GROUP));
	}

	public Group getUserGroup(String group) {
		String user_group = "cn=" + group + ",ou=groups";
		return groupRepo.findOne(LdapUtils.newLdapName(user_group));
	}

	public Group getAdminGroup() {
		return groupRepo.findOne(LdapUtils.newLdapName(ADMIN_GROUP));
	}

	@Override
	public void setBaseLdapPath(LdapName baseLdapPath) {
		this.basePath = baseLdapPath;
	}

	public List<User> findAll() {
		List<User> users = new ArrayList<User>();
		Iterable<User> iterableUsers = userRepo.findAll();
		for (User u : iterableUsers)
			users.add(u);
		return users;
	}

	public User findUser(String userId) {
		return userRepo.findOne(LdapUtils.newLdapName(userId));
	}

	public Group createGroup(Group group) {
		Group g = groupRepo.save(group);
		return g;

	}
	
	public Group updateGroup(Group group) {
		Group g = groupRepo.findByName(group.getName());
		g.setName(group.getNewName());
		groupRepo.save(g);
		return g;

	}

	public User createUser(User user) {
		List<String> groups = user.getGroups();
		user.setGroups(null);
		Group userGroup = null;
		userGroup = getUserGroup();
		User savedUser = userRepo.save(user);
		if (userGroup != null) {
			userGroup.addMember(toAbsoluteDn(savedUser.getId()));
			groupRepo.save(userGroup);
		}

		if (groups.size() > 0) {
			for (String group : groups) {

				userGroup = getUserGroup(group);
				userGroup.addMember(toAbsoluteDn(savedUser.getId()));
				groupRepo.save(userGroup);
			}
		}

		return savedUser;
	}

	public LdapName toAbsoluteDn(Name relativeName) {
		return LdapNameBuilder.newInstance(basePath).add(relativeName).build();
	}

	/**
	 * This method expects absolute DNs of group members. In order to find the
	 * actual users the DNs need to have the base LDAP path removed.
	 *
	 * @param absoluteIds
	 * @return
	 */
	// public Set<User> findAllMembers(Iterable<Name> absoluteIds) {
	// return
	// Sets.newLinkedHashSet(userRepo.findAll(toRelativeIds(absoluteIds)));
	// }
	//
	// public Iterable<Name> toRelativeIds(Iterable<Name> absoluteIds) {
	// return Iterables.transform(absoluteIds, new Function<Name, Name>() {
	// @Override
	// public Name apply(Name input) {
	// return LdapUtils.removeFirst(input, baseLdapPath);
	// }
	// });
	// }

	public User updateUser(User user) {
		User existingUser = findOneByUsername(user.getUsername());
		LdapName originalId = (LdapName) (existingUser.getId());
		if (user.getNewUsername() != null && !user.getNewUsername().isEmpty()
				&& !user.getNewUsername().equals(user.getUsername())) {
			user.setUsername(user.getNewUsername());
			user.setNewUsername(null);
		} else
			user.setNewUsername(null);

		if (user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
			String generatedSecuredPasswordHash = BCrypt.hashpw(user.getNewPassword(), BCrypt.gensalt(12));
			user.setUserPassword(generatedSecuredPasswordHash);
			user.setNewPassword(null);
		} else
			user.setUserPassword(user.getUserPasswordASCII());

		return updateUserAd(originalId, existingUser, user);

	}

	public User deleteUser(User user) {
		User existingUser = findOneByUsername(user.getUsername());
		LdapName originalId = (LdapName) (existingUser.getId());

		LdapName oldMemberDn = toAbsoluteDn(originalId);
		List<String> groups = groupRepoImpl.getUserGroups(user);

		userRepo.delete(originalId);

		for (String group : groups) {
			// groupRepoImpl.removeMemberFromGroup(group, existingUser);
			Group g = groupRepo.findByName(group);
			g.removeMember(oldMemberDn);

			groupRepo.save(g);
		}

		return existingUser;

	}

	/**
	 * Update the user and - if its id changed - update all group references to
	 * the user.
	 *
	 * @param originalId
	 *            the original id of the user.
	 * @param existingUser
	 *            the user, populated with new data
	 *
	 * @return the updated entry
	 */
	private User updateUserStandard(LdapName originalId, User existingUser) {
		LdapName oldMemberDn = toAbsoluteDn(originalId);
		Collection<Group> groups = groupRepo.findByMember(oldMemberDn);

		User savedUser = userRepo.save(existingUser);

		if (!originalId.equals(savedUser.getId())) {
			// The user has moved - we need to update group references.
			LdapName newMemberDn = toAbsoluteDn(savedUser.getId());

			updateGroupReferences(groups, oldMemberDn, newMemberDn);
		}
		return savedUser;
	}

	/**
	 * Special behaviour in AD forces us to get the group membership before the
	 * user is updated, because AD clears group membership for removed entries,
	 * which means that once the user is update we've lost track of which groups
	 * the user was originally member of, preventing us to update the membership
	 * references so that they point to the new DN of the user.
	 *
	 * This is slightly less efficient, since we need to get the group
	 * membership for all updates even though the user may not have been moved.
	 * Using our knowledge of which attributes are part of the distinguished
	 * name we can do this more efficiently if we are implementing specifically
	 * for Active Directory - this approach is just to highlight this quite
	 * significant difference.
	 *
	 * @param originalId
	 *            the original id of the user.
	 * @param existingUser
	 *            the user, populated with new data
	 *
	 * @return the updated entry
	 */
	private User updateUserAd(LdapName originalId, User existingUser, User user) {
		
		LdapName oldMemberDn = toAbsoluteDn(originalId);
		List<String> groups = groupRepoImpl.getUserGroups(existingUser);

		userRepo.delete(originalId);

		for (String group : groups) {
			// groupRepoImpl.removeMemberFromGroup(group, existingUser);
			Group g = groupRepo.findByName(group);
			g.removeMember(oldMemberDn);

			groupRepo.save(g);
		}

		List<String> userGroups = user.getGroups();
		user.setGroups(null);
		User savedUser = userRepo.save(user);
		LdapName newMemberDn = toAbsoluteDn(savedUser.getId());

		for (String group : userGroups) {
			Group g = groupRepo.findByName(group);
			g.addMember(newMemberDn);
			groupRepo.save(g);

		}
		return existingUser;
	}

	private void updateGroupReferences(Collection<Group> groups, Name originalId, Name newId) {
		for (Group group : groups) {
			group.removeMember(originalId);
			group.addMember(newId);

			groupRepo.save(group);
		}
	}

	public List<User> searchByNameName(String lastName) {
		return null; // userRepo.findByFullNameContains(lastName);
	}

	public User findOneByUsername(String username) {
		return userRepo.findOneByUsername(username);
	}
}