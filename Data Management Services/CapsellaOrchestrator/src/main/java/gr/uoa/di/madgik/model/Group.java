package gr.uoa.di.madgik.model;

import java.io.Serializable;
import javax.persistence.*;

import org.postgresql.jdbc.PgArray;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="groups")
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	private Integer id;

	@Column(name="rights")
	private String rights;

	public Group() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRights() {
		return this.rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public List<String> getListRights(){
		String temp_rights = rights.replace("{", "");
		temp_rights = temp_rights.replace("}", "");
		temp_rights = temp_rights.replace("\"", "");
		 return Arrays.asList(temp_rights.split(","));
	}

}