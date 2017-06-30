package gr.uoa.di.madgik.model;

public enum Roles {
	ADMIN("ADMIN"),
	READ("READ"),
	WRITE("WRITE");

	private final String role;

	public static String getADMIM()
	{
		return Roles.values()[0].toString();
	}
	
	public static String getRead()
	{
		return Roles.values()[1].toString();
	}
	
	public static String getWrite()
	{
		return Roles.values()[2].toString();
	}
	
	private Roles(String role) {
		this.role = role;
	}

}
