package gr.uoa.di.madgik.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the metadata database table.
 * 
 */
@ApiModel(value="metadata", description="Sample metada  model for the documentation")
@Entity
@NamedQuery(name="Metadata.findAll", query="SELECT m FROM Metadata m")
public class Metadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@GeneratedValue(generator = "uuid2")
	@Column
	private String uuid;

	@JsonProperty(required = true)
    @ApiModelProperty(notes = "The name of author", required = true)
	@Column
	private String author;

	@Column
	private String comments;

	
	@Column(name="content_type")
	private String contentType;

	@Column(name="dataset_group")
	private String datasetGroup;

	@Column(name="dataset_name")
	private String datasetName;


	@Column(name="last_updated")
	private Timestamp lastUpdated;

	@Column
	private Integer size;

	@Column
	private String tags;

	@Column(name="time_created")
	private Timestamp timeCreated;

	@Column(name="username")
	private String username;
	
	@Column(name="owner_group")
	private String ownerGroup;
	
	@Column(name="status")
	private String status;

	@Column
	private String access;
	
	@Column(name="author_email")
	private String authorEmail;

	@Column(name="maintainer")
	private String maintainer;

	@Column(name="maintainer_email")
	private String maintainerEmail;

	@Column(name="license")
	private String license;
	
	@Column(name="description")
	private String description;
	
	@Column(name="source")
	private String source;
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="metadata_uuid") 
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


	public List<Endpoint> getEndpoints() {
		return endpoints;
	}


	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}


	public String getDataset_group() {
		return datasetGroup;
	}


	public void setDataset_group(String dataset_group) {
		this.datasetGroup = dataset_group;
	}


	
	
}