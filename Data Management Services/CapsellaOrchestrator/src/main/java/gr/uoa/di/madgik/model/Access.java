package gr.uoa.di.madgik.model;

public enum Access {
	
	PUBLIC("public"),
	PROTECTED("protected"),
	PRIVATE("private");
	
	public final String access;
	
	 public static Access getValue(String type) 
	 {
		 switch(type)
		 {
		 	case "public":
				 return PUBLIC;
			case "protected":
				 return PROTECTED;
			case "private":
				 return PRIVATE;
			default:
				 return null;
		 }
	 }

	    public String toString() {
	    	
	    	switch(this)
	    	{
	    		
	    		case PUBLIC:
	    			return "public";
				case PROTECTED:
					return "protected";
	    		case PRIVATE:
	    			return "private";
	    		
	    	}
	       return "";
	    }
	
	private Access(String access) {
		this.access = access;
	}
	
	
}
