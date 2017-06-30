package gr.uoa.di.madgik.model;

public enum Role {
	ADMIN("ADMIN"),
	READ("READ"),
	WRITE("WRITE");

	private final String role;

	public static String getADMIM()
	{
		return Role.values()[0].toString();
	}
	
	public static String getRead()
	{
		return Role.values()[1].toString();
	}
	
	public static String getWrite()
	{
		return Role.values()[2].toString();
	}
	
	private Role(String role) {
		this.role = role;
	}
	
	 public static Role getValue(String role) 
	 {
		 
		 switch(role)
		 {
			 case "ADMIN":
				 return ADMIN;
			 case "READ":
				 return READ;
			 case "WRITE":
				 return WRITE;
			 default:
				 return null;
		 }
	 }
}
