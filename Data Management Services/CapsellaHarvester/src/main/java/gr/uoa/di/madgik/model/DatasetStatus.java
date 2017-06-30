package gr.uoa.di.madgik.model;

public enum DatasetStatus 
{
	ACTIVE("active"),
	INACTIVE("inactive"),
	UNDERUPDATE("under_update");

	public final String datasetStatus;

	 public static DatasetStatus getValue(String type) 
	 {
		 
		 switch(type)
		 {
			 case "active":
				 return ACTIVE;
			 case "inactive":
				 return INACTIVE;
			 case "under_update":
				 return UNDERUPDATE;
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
    		case UNDERUPDATE:
    			return "under_update";
    	}
       return "";
    }

	private DatasetStatus(String datasetStatus) {
		this.datasetStatus = datasetStatus;
	}
}