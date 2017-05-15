package gr.uoa.di.madgik.model;

import java.io.Serializable;

public class Rule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5911392658295576479L;

	private String userName;
	private String layer;
	private String workspace;
	private String priority;
	private String service;
	private String request;
	private String access;
	private String roleName;
	
	
	
	
	public Rule(String userName, String layer, String workspace, String service, String request, String access, String priority, String roleName) {
		super();
		this.userName = userName;
		this.layer = layer;
		this.workspace = workspace;
		this.service= service;
		this.request = request;
		this.access = access;
		this.roleName = roleName;
	}

	public Rule(String priority, String access) {
		this.priority = priority;
		this.access = access;
	}

	public Rule(String priority, String access, String roleName) {
		this.priority = priority;
		this.access = access;
		this.roleName = roleName;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
}
