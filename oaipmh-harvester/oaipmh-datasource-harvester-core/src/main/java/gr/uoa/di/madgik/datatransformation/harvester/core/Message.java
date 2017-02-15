package gr.uoa.di.madgik.datatransformation.harvester.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.GetRecord;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.Identify;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListIdentifiers;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListMetadataFormats;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListRecords;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.ListSets;

@JsonInclude(Include.NON_NULL)
public class Message {

	private String url;
	private String verb;
	
	private GetRecord getRecord;
	private Identify identify;
	private ListIdentifiers listIdentifiers;
	private ListMetadataFormats listMetadataFormats;
	private ListRecords listRecords;
	private ListSets listSets;

	public String getUrl() {
		return url;
	}
	public void setUrl(String URL) {
		this.url = URL;
	}
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public GetRecord getGetRecord() {
		return getRecord;
	}
	public void setGetRecord(GetRecord getRecord) {
		this.getRecord = getRecord;
	}
	
	public Identify getIdentify() {
		return identify;
	}
	public void setIdentify(Identify identify) {
		this.identify = identify;
	}
	
	public ListIdentifiers getListIdentifiers() {
		return listIdentifiers;
	}
	public void setListIdentifiers(ListIdentifiers listIdentifiers) {
		this.listIdentifiers = listIdentifiers;
	}
	
	public ListMetadataFormats getListMetadataFormats() {
		return listMetadataFormats;
	}
	public void setListMetadataFormats(ListMetadataFormats listMetadataFormats) {
		this.listMetadataFormats = listMetadataFormats;
	}
	
	public ListRecords getListRecords() {
		return listRecords;
	}
	public void setListRecords(ListRecords listRecords) {
		this.listRecords = listRecords;
	}
	
	public ListSets getListSets() {
		return listSets;
	}
	public void setListSets(ListSets listSets) {
		this.listSets = listSets;
	}
	
}