package gr.uoa.di.madgik.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * The persistent class for the endpoints database table.
 * 
 */
@Entity
@Table(name="endpoints")
@NamedQuery(name="Endpoint.findAll", query="SELECT e FROM Endpoint e")
public class Endpoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="endpoint_url")
	private String endpointUrl;
	
	@Column
	private String type;
	
	@ManyToOne @JsonBackReference
	private Metadata metadata;

	public Endpoint() {
	}
	
	
	public String getEndpointUrl() {
		return this.endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}


	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Metadata getMetadata() {
		return this.metadata;
	}
	
	public void setMetadata(Metadata metadata){
		this.metadata = metadata;
	}

}