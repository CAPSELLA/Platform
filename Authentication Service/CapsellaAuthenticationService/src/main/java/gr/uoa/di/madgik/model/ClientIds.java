package gr.uoa.di.madgik.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="clientIds")
public class ClientIds implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="name")
	private String name;
	
	@Column(name="hostname")
	private String hostname;
	
	@Column(name="groups")
	private String groups;
	
	@Column(name="token")
	private String token;
	
	@Column(name="expiration_date")
	private String expiration_date;
	

	public ClientIds() {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getGroups() {
		return groups;
	}


	public void setGroups(String groups) {
		this.groups = groups;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getExpiration_date() {
		return expiration_date;
	}


	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}
	
	

}