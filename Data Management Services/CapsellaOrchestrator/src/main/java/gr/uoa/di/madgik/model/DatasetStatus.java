package gr.uoa.di.madgik.model;

public enum DatasetStatus 
{
	ACTIVE("active"),
	INACTIVE("inactive"),
	ONUPDATE("on_update");

	public final String datasetStatus;

	 public static DatasetStatus getValue(String type) 
	 {
		 
		 switch(type)
		 {
			 case "active":
				 return ACTIVE;
			 case "inactive":
				 return INACTIVE;
			 case "on_update":
				 return ONUPDATE;
			 default:
				 return null;
		 }
	 }

    public String toString() {
    	
    	switch(this)
    	{
    		
    		case ACTIVE:
    			return "active";
    		case INACTIVE:
    			return "inactive";
    		case ONUPDATE:
    			return "on_update";
    	}
       return "";
    }

	private DatasetStatus(String datasetStatus) {
		this.datasetStatus = datasetStatus;
	}
}