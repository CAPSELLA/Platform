package gr.uoa.di.madgik.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import eu.trentorise.opendata.jackan.internal.org.apache.http.entity.ContentType;


/**
 * The persistent class for the metadata database table.
 * 
 */

public class Metadata implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String uuid;

	private String author;

	private String comments;

	private String contentType;
	
	private String datasetName;

	private Timestamp lastUpdated;

	private Integer size;

	private String tags;

	private Timestamp timeCreated;

	private String username;
	
	private String ownerGroup;

	private String status;

	private String access;
	
	private String authorEmail;

	private String maintainer;

	private String maintainerEmail;

	private String license;
	
	private String description;

	private String source;
	
	@JsonManagedReference
    private List<Endpoint> endpoints = new ArrayList();

	
	public Metadata() {
	}

	
	
	public String getAuthorEmail() {
		return authorEmail;
	}



	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}



	public String getMaintainer() {
		return maintainer;
	}



	public void setMaintainer(String maintainer) {
		this.maintainer = maintainer;
	}



	public String getMaintainerEmail() {
		return maintainerEmail;
	}



	public void setMaintainerEmail(String maintainerEmail) {
		this.maintainerEmail = maintainerEmail;
	}



	public String getLicense() {
		return license;
	}



	public void setLicense(String license) {
		this.license = license;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	
	public String getDatasetName() {
		return this.datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public List<Endpoint> getEndpoint() {
		return this.endpoints;
	}

	public void setEndpoint(List<Endpoint> endpoint) {
		this.endpoints = endpoint;
	}

	public Timestamp getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}


	public Timestamp getTimeCreated() {
		return this.timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setContentType(ContentType value) {
		
		
	}

	public String getOwnerGroup() {
		return ownerGroup;
	}

	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}
	
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	
	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}
}