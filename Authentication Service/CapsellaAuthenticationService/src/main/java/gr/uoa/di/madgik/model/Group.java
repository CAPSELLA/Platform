package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gr.uoa.di.madgik.repo.GroupRepo;
import gr.uoa.di.madgik.repo.GroupRepoImpl;


@Entry(objectClasses = {"posixGroup", "top"}, base = "ou=groups")
public final class Group implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
    private Name id;

    @Attribute(name = "cn")
    @DnAttribute(value = "cn", index=1)
    private String name;

    @Attribute(name = "description")
    private String description;

    
    @Attribute(name = "gidNumber")
    private String gidNumber;

    private List<String> roles;
    
    private String newName;
    
    public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}

	@Attribute(name = "memberUid")
    private Set<Name> members = new HashSet<Name>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Name> getMembers() {
        return  members;
    }

    public void addMember(Name newMember) {
        members.add(newMember);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void getRoles(List<String> rights) {

	
//		List<String> roles = new ArrayList<String>();
//		if (rights.equals(Roles.ADMIN.name()))
//		{
//			roles.add(Roles.ADMIN.name());
//			roles.add(Roles.WRITE.name());
//			roles.add(Roles.READ.name());
//			
//		}
//		else if (rights.equals(Roles.WRITE.name()))
//		{
//			roles.add(Roles.WRITE.name());
//			roles.add(Roles.READ.name());
//			
//		}
//		else if (rights.equals(Roles.READ.name()))
//		{
//			
//			roles.add(Roles.READ.name());
//			
//		}

		setRoles(rights);
	}
}
