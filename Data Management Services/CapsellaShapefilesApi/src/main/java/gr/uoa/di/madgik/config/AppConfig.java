package gr.uoa.di.madgik.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
	
	@Autowired
	private Environment environment;
	
	
	public GeoserverTemplate geoserverTemplate;
	
	
	
    public GeoserverTemplate getProperties() {
    	
    	System.out.println("I'm in config and im instantiating");
    	
    	return new GeoserverTemplate(environment.getRequiredProperty("geoserver.username"), 
    			environment.getRequiredProperty("geoserver.password"), environment.getRequiredProperty("geoserver.datastore"),
    			environment.getRequiredProperty("geoserver.workspace"),environment.getRequiredProperty("geoserver.baseUrl"),
    			environment.getRequiredProperty("geoserver.wmsURL"), environment.getRequiredProperty("geoserver.wfsURL"),
    			 environment.getRequiredProperty("geoserver.addUserURL"), environment.getRequiredProperty("geoserver.addRuleURL"));
    }
    
    public class GeoserverTemplate{
    	private String username;
    	private String password;
    	private String dsName;
    	private String workspace;
    	private String gsBaseUrl;
    	private String wmsUrl;
    	private String wfsUrl;
    	private String addUserURL;
    	private String addRuleURL;
    	
    	
    	

		public GeoserverTemplate(String username, String password, String dsName, String workspace, String gsBaseUrl, String wmsUrl, String wfsUrl, String addUserUrl, String addRuleUrl) {
			super();
			this.username = username;
			this.password = password;
			this.dsName = dsName;
			this.workspace = workspace;
			this.gsBaseUrl = gsBaseUrl;
			this.wmsUrl = wmsUrl;
			this.wfsUrl = wfsUrl;
			this.addUserURL = addUserUrl;
			this.addRuleURL = addRuleUrl;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getDsName() {
			return dsName;
		}

		public void setDsName(String dsName) {
			this.dsName = dsName;
		}

		public String getWorkspace() {
			return workspace;
		}

		public void setWorkspace(String workspace) {
			this.workspace = workspace;
		}

		public String getGsBaseUrl() {
			return gsBaseUrl;
		}

		public void setGsBaseUrl(String gsBaseUrl) {
			this.gsBaseUrl = gsBaseUrl;
		}

		public String getWmsUrl() {
			return wmsUrl;
		}

		public void setWmsUrl(String wmsUrl) {
			this.wmsUrl = wmsUrl;
		}

		public String getWfsUrl() {
			return wfsUrl;
		}

		public void setWfsUrl(String wfsUrl) {
			this.wfsUrl = wfsUrl;
		}

		public String getAddUserURL() {
			return addUserURL;
		}

		public void setAddUserURl(String addUser) {
			this.addUserURL = addUser;
		}

		public String getAddRuleURL() {
			return addRuleURL;
		}

		public void setAddRuleURL(String addRuleURL) {
			this.addRuleURL = addRuleURL;
		}

		public void setAddUserURL(String addUserURL) {
			this.addUserURL = addUserURL;
		}
    	
    	
    	
    }
}
