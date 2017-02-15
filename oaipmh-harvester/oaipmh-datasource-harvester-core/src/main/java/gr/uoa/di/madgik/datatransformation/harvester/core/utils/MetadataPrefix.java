package gr.uoa.di.madgik.datatransformation.harvester.core.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MetadataPrefix {
	oai_dc((short)0),
	marc21((short)1),
	rfc1807((short)2),
	oai_marc((short)3),
	arXiv((short)4),
	ese((short)5);
	
	private short code;
	
	private static final Map<Short,MetadataPrefix> lookup  = new HashMap<Short, MetadataPrefix>();
	 
	static {
	      for(MetadataPrefix it : EnumSet.allOf(MetadataPrefix.class))
	           lookup.put(it.code(), it);
	 }
	
	MetadataPrefix() { }
	
	MetadataPrefix(short code) {
		this.code = code;
	}
	
	public short code() { return code; }

	private void setCode(short code) { this.code = code; }
	
	public static MetadataPrefix fromCode(short code) {
		return lookup.get(code);
	}
};