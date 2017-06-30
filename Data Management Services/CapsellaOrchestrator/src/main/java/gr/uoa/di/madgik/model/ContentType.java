package gr.uoa.di.madgik.model;

public enum ContentType {

	CSV("csv"),
	JSON("json"),
	SHAPEFILE("shapefile"),
	RELATIONAL("relational");
	
	public final String contentType;
	
	 public static ContentType getValue(String type) 
	 {
		 
		 switch(type)
		 {
			 case "csv":
				 return CSV;
			 case "json":
				 return JSON;
			 case "shapefile":
				 return SHAPEFILE;
			 case "relational":
				 return RELATIONAL;
			 default:
				 return null;
		 }
	 }

	    public String toString() {
	    	
	    	switch(this)
	    	{
	    		
	    		case CSV:
	    			return "csv";
	    		case JSON:
	    			return "json";
	    		case SHAPEFILE:
	    			return "shapefile";
	    		case RELATIONAL:
	    			return "relational";
	    	}
	       return "";
	    }
	
	private ContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
