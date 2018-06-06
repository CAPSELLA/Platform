package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;
import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gr.uoa.di.madgik.repo.GroupRepo;
import gr.uoa.di.madgik.repo.GroupRepoImpl;
import gr.uoa.di.madgik.service.GroupService;

@Entry(objectClasses = {  "inetOrgPerson", "posixAccount", "top" }, base = "ou=users,ou=capsella")
public final class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4921631937954243400L;

	@JsonIgnore
	@Id
	private Name id;
	
	@Attribute(name = "cn")
    @DnAttribute(value = "cn", index = 1)
    private String name;


	@Attribute(name = "uid")
//	@DnAttribute(value = "uid", index=1)
	private String username;

	@Attribute(name = "cn")
	private String fullName;

//	@Attribute(name = "displayName")
//	private String displayName;

//	@Attribute(name = "givenName")
//	private String firstName;

	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "gidNumber")
	private String gidNumber;

	@Attribute(name = "uidNumber")
	private String uidNumber;

	@Attribute(name = "homeDirectory")
	private String homeDirectory;
	
	@Attribute(name = "mail")
	private String email;

	@Attribute(name = "userPassword")
	private String userPassword;
	
	private String newUsername, newPassword;

	private List<String> groups = new ArrayList<>();

	public String getUserPassword() {

		return userPassword;

	}
	
	
	

	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getNewPassword() {
		return newPassword;
	}



	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}



	public String getNewUsername() {
		return newUsername;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}

	@JsonIgnore
	public String getUserPasswordASCII() {

		String password = ASCIIConvertFunction();
		return password;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

//	public String getDisplayName() {
//		return displayName;
//	}
//
//	public void setDisplayName(String displayName) {
//		this.displayName = displayName;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}

	public String getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}

	public Name getId() {
		return id;
	}

	public void setId(Name id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = LdapUtils.newLdapName(id);
	}

//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;

		if (id != null ? !id.equals(user.id) : user.id != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getGroups() {

		return groups;
	}

	private String ASCIIConvertFunction() {
		StringBuilder sb = new StringBuilder();
		String[] split = userPassword.split(",");
		for (String s : split) {
			int c = Integer.parseInt(s);
			sb.append(Character.toString((char) c));
		}
		String password = sb.toString();
		return password;
	}

	@Autowired
	public void getRoles(GroupRepo groupRepo, GroupRepoImpl groupRepoImpl, GroupService groupService) {

		//List<String> roles = new ArrayList<String>();

		List<String> roles = groupRepoImpl.getUserGroups(this);
//		for(String group : groupNames)
//		{
////			if(groupRepoImpl.isMemberInGroup(GroupRepo.ADMIN_GROUP_STRING, this));
////			{
//				Group g = groupService.getGroup(group);
//				
//				for(String right : g.getRoles())
//				{
//					if((Roles.ADMIN.name()).equals(right))
//						roles.add(Roles.ADMIN.name());
//					else if ((Roles.READ.name()).equals(right))
//						roles.add(Roles.READ.name());
//					else if((Roles.WRITE.name()).equals(right))
//						roles.add(Roles.WRITE.name());
//					
//				}
////			}
//			
//		}
		
		Set<String> hs = new HashSet<>();
		hs.addAll(roles);
		roles.clear();
		roles.addAll(hs);
		
		
		setGroups(roles);
	}
	
	
	public void getGroups(GroupRepo groupRepo, GroupRepoImpl groupRepoImpl, GroupService groupService) {

		List<String> roles = new ArrayList<String>();
		
		roles = groupRepoImpl.getUserGroups(this);

		setGroups(roles);
	}

}