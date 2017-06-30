package gr.uoa.di.madgik.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the endpoints database table.
 * 
 */

public class Endpoint  {

	
	private String endpointUrl;

	private String uuid;
	
	@JsonBackReference
	private Metadata metadata;
	
	private String type;


	public Endpoint() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getEndpointUrl() {
		return this.endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Metadata getMetadata() {
		return this.metadata;
	}
	
	public void setMetadata(Metadata metadata){
		this.metadata = metadata;
	}

}