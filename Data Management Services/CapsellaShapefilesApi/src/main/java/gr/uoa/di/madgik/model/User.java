package gr.uoa.di.madgik.model;

import java.io.Serializable;

import javax.persistence.Entity;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3504922420370277122L;
	
	private String userName;
	private String password;
	private boolean enabled;
	
	
	public User(String userName, String password, boolean enabled) {
		super();
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

}
