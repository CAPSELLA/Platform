package gr.uoa.di.madgik.model;

public enum EndpointType {

	NATIVE("native"),
	SERVICE("service");
	
	public final String endpointType;
	
	 public static EndpointType getValue(String type) 
	 {
		 
		 switch(type)
		 {
			 case "native":
				 return NATIVE;
			 case "service":
				 return SERVICE;
			 default:
				 return null;
		 }
	 }

	    public String toString() {
	    	
	    	switch(this)
	    	{
	    		
	    		case NATIVE:
	    			return "native";
	    		case SERVICE:
	    			return "service";
	    	}
	       return "";
	    }
	
	private EndpointType(String endpointType) {
		this.endpointType = endpointType;
	}
}
